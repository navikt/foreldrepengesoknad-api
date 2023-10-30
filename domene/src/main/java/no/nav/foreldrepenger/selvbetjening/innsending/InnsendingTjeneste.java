package no.nav.foreldrepenger.selvbetjening.innsending;

import static org.slf4j.LoggerFactory.getLogger;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsending.pdf.PdfGenerator;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.SøknadV2Dto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.TilbakebetalingUttalelseDto;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.KryptertMellomlagring;

@Service
public class InnsendingTjeneste implements RetryAware {

    private static final String RETURNERER_KVITTERING = "Returnerer kvittering {}. Innsending tok {}ms";
    private static final Logger LOG = getLogger(InnsendingTjeneste.class);
    private static final Random IDGENERATOR = new SecureRandom();
    private final InnsendingConnection connection;
    private final KryptertMellomlagring mellomlagring;
    private final PdfGenerator pdfGenerator;

    public InnsendingTjeneste(InnsendingConnection connection,
                              KryptertMellomlagring mellomlagring,
                              PdfGenerator pdfGenerator) {
        this.connection = connection;
        this.mellomlagring = mellomlagring;
        this.pdfGenerator = pdfGenerator;
    }

    public Kvittering sendInn(SøknadDto søknad) {
        LOG.info("Sender inn søknad av type {}", søknad.type());
        var start = Instant.now();
        hentMellomlagredeFiler(søknad.vedlegg());
        var kvittering = connection.sendInn(søknad);
        slettMellomlagringOgSøknad(søknad.vedlegg());
        var finish = Instant.now();
        var ms = Duration.between(start, finish).toMillis();
        LOG.info(RETURNERER_KVITTERING, kvittering, ms);
        return kvittering;
    }

    public Kvittering sendInn(SøknadV2Dto søknad) {
        LOG.info("Sender inn søknad av type {}", søknad.type());
        var start = Instant.now();
        hentMellomlagredeFiler(søknad.vedlegg());
        var kvittering = connection.sendInn(søknad);
        slettMellomlagringOgSøknad(søknad.vedlegg());
        var finish = Instant.now();
        var ms = Duration.between(start, finish).toMillis();
        LOG.info(RETURNERER_KVITTERING, kvittering, ms);
        return kvittering;
    }

    public Kvittering ettersend(EttersendelseDto e) {
        LOG.info("Ettersender for sak {}", e.saksnummer());
        var start = Instant.now();
        hentMellomlagredeFiler(e.vedlegg());
        if (e.erTilbakebetalingUttalelse()) {
            LOG.info("Konverterer tekst til vedleggs-pdf {}", e.brukerTekst().dokumentType());
            e.vedlegg().add(vedleggFra(uttalelseFra(e)));
        }
        var kvittering = connection.ettersend(e);
        e.vedlegg().forEach(mellomlagring::slettKryptertVedlegg);
        var finish = Instant.now();
        var ms = Duration.between(start, finish).toMillis();
        LOG.info(RETURNERER_KVITTERING, kvittering, ms);
        return kvittering;
    }

    public Kvittering endre(EndringssøknadDto es) {
        LOG.info("Endrer søknad av type {}", es.type());
        var start = Instant.now();
        hentMellomlagredeFiler(es.vedlegg());
        var kvittering = connection.endre(es);
        slettMellomlagringOgSøknad(es.vedlegg());
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

    private void slettMellomlagringOgSøknad(List<VedleggDto> vedlegg) {
        LOG.info("Sletter mellomlagret søknad og vedlegg");
        vedlegg.forEach(mellomlagring::slettKryptertVedlegg);
        mellomlagring.slettKryptertSøknad();
        LOG.info("Slettet mellomlagret søknad og vedlegg OK");
    }

    private void hentVedleggBytes(VedleggDto vedlegg) {
        if (vedlegg.getUrl() != null) {
            vedlegg.setContent(mellomlagring.lesKryptertVedlegg(vedlegg.getUuid())
                .map(a -> a.bytes)
                .orElse(new byte[] {}));
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + ", mellomlagring=" + mellomlagring + "]";
    }


}
