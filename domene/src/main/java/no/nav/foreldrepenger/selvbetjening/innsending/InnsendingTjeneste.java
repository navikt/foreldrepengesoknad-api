package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.selvbetjening.innsending.VedleggReferanseMapperTjeneste.leggVedleggsreferanserTilSøknad;
import static org.slf4j.LoggerFactory.getLogger;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsending.pdf.PdfGenerator;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.TilbakebetalingUttalelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.KryptertMellomlagring;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Ytelse;

@Service
public class InnsendingTjeneste implements RetryAware {
    private static final Logger SECURE_LOGGER = getLogger("secureLogger");
    private static final String RETURNERER_KVITTERING = "Returnerer kvittering {}. Innsending tok {}ms";
    private static final Logger LOG = getLogger(InnsendingTjeneste.class);
    private static final Random IDGENERATOR = new SecureRandom();
    private final InnsendingConnection connection;
    private final KryptertMellomlagring mellomlagring;
    private final PdfGenerator pdfGenerator;
    private final ObjectMapper mapper;

    public InnsendingTjeneste(InnsendingConnection connection,
                              KryptertMellomlagring mellomlagring,
                              PdfGenerator pdfGenerator, ObjectMapper mapper) {
        this.connection = connection;
        this.mellomlagring = mellomlagring;
        this.pdfGenerator = pdfGenerator;
        this.mapper = mapper;
    }

    public Kvittering sendInn(Innsending innsending) {
        LOG.info("Mottok {} med {} vedlegg", innsending.navn(), innsending.vedlegg().size());
        SECURE_LOGGER.info("Mottatt {} fra frontend med følende innhold: {}", innsending.navn(), tilJson(innsending));
        var start = Instant.now();
        hentMellomlagredeFiler(innsending);
        leggVedleggsreferanserTilSøknad(innsending);
        var kvittering = connection.sendInn(innsending);
        mellomlagring.slettMellomlagring(tilYtelse(innsending)); // Deprecated (kan fjernes etter frontend har implementert sletting mellomlagring for alle ytelsene)
        var finish = Instant.now();
        var ms = Duration.between(start, finish).toMillis();
        LOG.info(RETURNERER_KVITTERING, kvittering, ms);
        return kvittering;
    }

    public Kvittering ettersend(EttersendelseDto ettersendelse) {
        LOG.info("Mottok ettersendelse med {} vedlegg", ettersendelse.vedlegg().size());
        var start = Instant.now();
        hentMellomlagredeFiler(ettersendelse);
        if (ettersendelse.erTilbakebetalingUttalelse()) {
            LOG.info("Konverterer tekst til vedleggs-pdf {}", ettersendelse.brukerTekst().dokumentType());
            ettersendelse.vedlegg().add(vedleggFra(uttalelseFra(ettersendelse)));
        }
        var kvittering = connection.ettersend(ettersendelse);
        var finish = Instant.now();
        var ms = Duration.between(start, finish).toMillis();
        LOG.info(RETURNERER_KVITTERING, kvittering, ms);
        return kvittering;
    }


    private VedleggDto vedleggFra(TilbakebetalingUttalelseDto u) {
        return new VedleggDto(pdfGenerator.generate(u), "Tekst fra bruker", id(), u.brukerTekst().dokumentType());
    }

    private static TilbakebetalingUttalelseDto uttalelseFra(EttersendelseDto e) {
        return new TilbakebetalingUttalelseDto(
            e.type(),
            e.saksnummer(),
            e.dialogId(),
            e.brukerTekst());
    }

    private static MutableVedleggReferanseDto id() {
        return new MutableVedleggReferanseDto("V" + IDGENERATOR.nextLong());
    }

    public void hentMellomlagredeFiler(Innsending innsending) {
        var vedlegg = innsending.vedlegg();
        if (!vedlegg.isEmpty()) {
            var start = Instant.now();
            LOG.info("Henter mellomlagring for {} vedlegg", vedlegg.size());
            for (var vedleggDto : vedlegg) {
                hentVedleggBytes(vedleggDto, innsending);
            }
            var finish = Instant.now();
            var ms = Duration.between(start, finish).toMillis();
            LOG.info("Hentet mellomlagring OK for {} vedlegg ({}ms)", vedlegg.size(), ms);
        }
    }

    private void hentVedleggBytes(VedleggDto vedlegg, Innsending innsending) {
        if (vedlegg.getUrl() != null) {
            vedlegg.setContent(mellomlagring.lesKryptertVedlegg(vedlegg.getUuid(), tilYtelse(innsending)).orElse(new byte[] {}));
        }
    }

    private Ytelse tilYtelse(Innsending innsending) {
        if (innsending instanceof ForeldrepengesøknadDto || innsending instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.ForeldrepengesøknadDto) {
            return Ytelse.FORELDREPENGER;
        }
        if (innsending instanceof EngangsstønadDto || innsending instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad.EngangsstønadDto) {
            return Ytelse.ENGANGSSTONAD;
        }
        if (innsending instanceof SvangerskapspengesøknadDto || innsending instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto) {
            return Ytelse.SVANGERSKAPSPENGER;
        }
        if (innsending instanceof EndringssøknadForeldrepengerDto || innsending instanceof no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad.EndringssøknadForeldrepengerDto) {
            return Ytelse.FORELDREPENGER;
        }
        if (innsending instanceof EttersendelseDto ettersendelse) {
            return switch (ettersendelse.type()) {
                case FORELDREPENGER -> Ytelse.FORELDREPENGER;
                case SVANGERSKAPSPENGER -> Ytelse.SVANGERSKAPSPENGER;
                case ENGANGSSTØNAD -> Ytelse.ENGANGSSTONAD;
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
