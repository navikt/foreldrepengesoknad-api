package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggInnsendingType.AUTOMATISK;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggInnsendingType.LASTET_OPP;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggUtil.mediaType;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_PDF;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsending.pdf.PdfGenerator;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggReferanse;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.TilbakebetalingUttalelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.KryptertMellomlagring;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.YtelseMellomlagringType;

@Service
public class InnsendingTjeneste implements RetryAware {
    private static final Logger SECURE_LOGGER = getLogger("secureLogger");
    private static final String RETURNERER_KVITTERING = "Returnerer kvittering {}. Innsending tok {}ms";
    private static final Logger LOG = getLogger(InnsendingTjeneste.class);
    private final InnsendingConnection connection;
    private final KryptertMellomlagring mellomlagring;
    private final PdfGenerator pdfGenerator;
    private final ObjectMapper mapper;

    public InnsendingTjeneste(InnsendingConnection connection, KryptertMellomlagring mellomlagring, PdfGenerator pdfGenerator, ObjectMapper mapper) {
        this.connection = connection;
        this.mellomlagring = mellomlagring;
        this.pdfGenerator = pdfGenerator;
        this.mapper = mapper;
    }

    public Kvittering sendInn(Innsending innsending) {
        LOG.info("Mottok {} med {} vedlegg", innsending.navn(), innsending.vedlegg().size());
        SECURE_LOGGER.info("Mottatt {} fra frontend med følende innhold: {}", innsending.navn(), tilJson(innsending));
        var start = Instant.now();
        var vedleggsinnhold = hentMellomlagredeVedlegg(innsending);
        validerVedlegg(innsending);
        var kvittering = connection.sendInn(innsending, vedleggsinnhold);
        var finish = Instant.now();
        var ms = Duration.between(start, finish).toMillis();
        LOG.info(RETURNERER_KVITTERING, kvittering, ms);
        return kvittering;
    }

    public Kvittering ettersend(EttersendelseDto ettersendelse) {
        LOG.info("Mottok ettersendelse med {} vedlegg", ettersendelse.vedlegg().size());
        var start = Instant.now();
        var vedleggsinnhold = hentMellomlagredeVedlegg(ettersendelse);
        if (ettersendelse.erTilbakebetalingUttalelse()) {
            genererPDFForUttalelseOmTilbakekrevingOgLeggTilIVedlegg(ettersendelse, vedleggsinnhold);
        }
        var kvittering = connection.ettersend(ettersendelse, vedleggsinnhold);
        var finish = Instant.now();
        var ms = Duration.between(start, finish).toMillis();
        LOG.info(RETURNERER_KVITTERING, kvittering, ms);
        return kvittering;
    }

    private void genererPDFForUttalelseOmTilbakekrevingOgLeggTilIVedlegg(EttersendelseDto ettersendelse,
                                                                         Map<VedleggReferanse, byte[]> vedleggsinnhold) {
        LOG.info("Konverterer tekst til vedleggs-pdf {}", ettersendelse.brukerTekst().dokumentType());
        var uttalelse = uttalelseFra(ettersendelse);
        var uttalelseVedlegg = vedleggFra(uttalelse);
        ettersendelse.vedlegg().add(uttalelseVedlegg);
        vedleggsinnhold.put(uttalelseVedlegg.referanse(), pdfGenerator.generate(uttalelse));
    }

    private static VedleggDto vedleggFra(TilbakebetalingUttalelseDto u) {
        return new VedleggDto(null, u.brukerTekst().dokumentType(), LASTET_OPP, "Tekst fra bruker", null);
    }

    private static TilbakebetalingUttalelseDto uttalelseFra(EttersendelseDto e) {
        return new TilbakebetalingUttalelseDto(e.type(), e.saksnummer(), e.dialogId(), e.brukerTekst());
    }

    private void validerVedlegg(Innsending innsending) {
        var antallAutomatiskeVedlegg = innsending.vedlegg().stream().filter(v -> AUTOMATISK.equals(v.innsendingsType())).count();
        if (antallAutomatiskeVedlegg > 0) {
            LOG.info("Innsending {} har {} automatiske vedlegg", innsending.navn(), antallAutomatiskeVedlegg);
            if (!YtelseMellomlagringType.FORELDREPENGER.equals(tilYtelse(innsending))) {
                throw new IllegalStateException("Utviklerfeil: Innsending av type " + innsending.navn() + " har automatiske vedlegg");
            }
        }
    }

    private Map<VedleggReferanse, byte[]> hentMellomlagredeVedlegg(Innsending innsending) {
        var ytelse = tilYtelse(innsending);
        var vedleggsinnhold = new HashMap<VedleggReferanse, byte[]>();

        LOG.info("Henter mellomlagrede vedlegg for {} ", innsending.navn());
        var start = Instant.now();
        innsending.påkrevdeVedlegg().stream().filter(VedleggDto::erOpplastetVedlegg).forEach(v -> {
            var innhold = vedleggFraMellomlagring(v.uuid(), ytelse);
            guardIkkePDF(innhold);
            vedleggsinnhold.put(v.referanse(), innhold);
        });
        var finish = Instant.now();
        var ms = Duration.between(start, finish).toMillis();
        LOG.info("Hentet mellomlagring OK for {} vedlegg ({}ms)", vedleggsinnhold.size(), ms);
        return vedleggsinnhold;
    }

    private byte[] vedleggFraMellomlagring(UUID uuid, YtelseMellomlagringType ytelse) {
        return mellomlagring.lesKryptertVedlegg(uuid.toString(), ytelse)
            .orElseThrow(() -> new IllegalStateException("Fant ikke mellomlagret vedlegg med uuid " + uuid + " og ytelse " + ytelse));
    }

    private static void guardIkkePDF(byte[] vedlegg) {
        var mediatype = mediaType(vedlegg);
        if (!APPLICATION_PDF.equals(mediatype)) {
            throw new IllegalStateException("Utviklerfeil: Mottok noe annet en PDF ved innsending " + mediatype);
        }
    }

    private YtelseMellomlagringType tilYtelse(Innsending innsending) {
        if (innsending instanceof ForeldrepengesøknadDto || innsending instanceof EndringssøknadForeldrepengerDto) {
            return YtelseMellomlagringType.FORELDREPENGER;
        }
        if (innsending instanceof EngangsstønadDto) {
            return YtelseMellomlagringType.ENGANGSSTONAD;
        }
        if (innsending instanceof SvangerskapspengesøknadDto) {
            return YtelseMellomlagringType.SVANGERSKAPSPENGER;
        }
        if (innsending instanceof EttersendelseDto ettersendelse) {
            return switch (ettersendelse.type()) {
                case FORELDREPENGER -> YtelseMellomlagringType.FORELDREPENGER;
                case SVANGERSKAPSPENGER -> YtelseMellomlagringType.SVANGERSKAPSPENGER;
                case ENGANGSSTØNAD -> YtelseMellomlagringType.ENGANGSSTONAD;
            };
        }
        throw new IllegalStateException("Utviklerfeil: Fant ikke ytelse på innsending: " + innsending);
    }

    private String tilJson(Innsending innsending) {
        try {
            return mapper.writeValueAsString(innsending);
        } catch (JsonProcessingException e) {
            return innsending.toString();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + ", mellomlagring=" + mellomlagring + "]";
    }


}
