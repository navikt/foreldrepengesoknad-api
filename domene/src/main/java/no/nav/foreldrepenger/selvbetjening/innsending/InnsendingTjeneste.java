package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.selvbetjening.innsending.VedleggsHåndteringTjeneste.fjernDupliserteVedleggFraInnsending;
import static org.slf4j.LoggerFactory.getLogger;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsending.pdf.PdfGenerator;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.TilbakebetalingUttalelseDto;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.KryptertMellomlagring;

@Service
public class InnsendingTjeneste implements RetryAware {
    private static final Logger SECURE_LOGGER = LoggerFactory.getLogger("secureLogger");
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
        var alleVedleggKopi = innsending.vedlegg().stream().toList();
        hentMellomlagredeFiler(innsending.vedlegg());
        fjernDupliserteVedleggFraInnsending(innsending);
        if (innsending instanceof EttersendelseDto e && e.erTilbakebetalingUttalelse()) {
            LOG.info("Konverterer tekst til vedleggs-pdf {}", e.brukerTekst().dokumentType());
            e.vedlegg().add(vedleggFra(uttalelseFra(e)));
        }
        var kvittering = connection.sendInn(innsending);
        slettMellomlagredeVedleggOgSøknad(alleVedleggKopi);
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

    public void hentMellomlagredeFiler(List<VedleggDto> vedlegg) {
        if (!vedlegg.isEmpty()) {
            var start = Instant.now();
            LOG.info("Henter mellomlagring for {} vedlegg", vedlegg.size());
            vedlegg.forEach(this::hentVedleggBytes);
            var finish = Instant.now();
            var ms = Duration.between(start, finish).toMillis();
            LOG.info("Hentet mellomlagring OK for {} vedlegg ({}ms)", vedlegg.size(), ms);
        }
    }

    @Deprecated
    private void slettMellomlagredeVedleggOgSøknad(List<VedleggDto> vedlegg) {
        try {
            LOG.info("Sletter mellomlagret søknad og vedlegg");
            vedlegg.forEach(mellomlagring::slettKryptertVedlegg);
            mellomlagring.slettKryptertSøknad();
            LOG.info("Slettet mellomlagret søknad og vedlegg OK");
        } catch (Exception e) {
            LOG.warn("Noe gikk galt med sletting av mellomlagret søknad/vedlegg", e);
        }
    }

    private void hentVedleggBytes(VedleggDto vedlegg) {
        if (vedlegg.getUrl() != null) {
            vedlegg.setContent(mellomlagring.lesKryptertVedlegg(vedlegg.getUuid())
                .map(a -> a.bytes)
                .orElse(new byte[] {}));
        }
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
