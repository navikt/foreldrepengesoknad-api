package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.selvbetjening.vedlegg.DelegerendeVedleggSjekker.DELEGERENDE;
import static org.slf4j.LoggerFactory.getLogger;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.tilbakebetaling.TilbakebetalingUttalelse;
import no.nav.foreldrepenger.selvbetjening.innsending.pdf.PdfGenerator;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.KryptertMellomlagring;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "false", matchIfMissing = true)
public class InnsendingTjeneste implements Innsending {

    private static final String RETURNERER_KVITTERING = "Returnerer kvittering {}";
    private static final Logger LOG = getLogger(InnsendingTjeneste.class);
    private static final Random IDGENERATOR = new SecureRandom();
    private final InnsendingConnection connection;
    private final KryptertMellomlagring mellomlagring;
    private final VedleggSjekker vedleggSjekker;
    private final PdfGenerator pdfGenerator;

    public InnsendingTjeneste(InnsendingConnection connection, KryptertMellomlagring mellomlagring,
            @Qualifier(DELEGERENDE) VedleggSjekker vedleggSjekker, PdfGenerator pdfGenerator) {
        this.connection = connection;
        this.mellomlagring = mellomlagring;
        this.vedleggSjekker = vedleggSjekker;
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    public Kvittering sendInn(Søknad søknad) {
        LOG.info("Sender inn søknad av type {}", søknad.getType());
        hentOgSjekk(søknad.getVedlegg());
        var kvittering = connection.sendInn(søknad);
        slettMellomlagringOgSøknad(søknad);
        LOG.info(RETURNERER_KVITTERING, kvittering);
        return kvittering;
    }

    @Override
    public Kvittering ettersend(Ettersending e) {
        LOG.info("Ettersender for sak {}", e.getSaksnummer());
        hentOgSjekk(e.getVedlegg());
        if (e.getDialogId() != null) {
            LOG.info("Konverterer tekst til vedleggs-pdf {}", e.getBrukerTekst().getDokumentType());
            e.getVedlegg().add(vedleggFra(uttalelseFra(e)));
        }
        var kvittering = connection.ettersend(e);
        e.getVedlegg().forEach(mellomlagring::slettKryptertVedlegg);
        LOG.info(RETURNERER_KVITTERING, kvittering);
        return kvittering;
    }

    @Override
    public Kvittering endre(Søknad es) {
        LOG.info("Endrer søknad av type {}", es.getType());
        hentOgSjekk(es.getVedlegg());
        var kvittering = connection.endre(es);
        slettMellomlagringOgSøknad(es);
        LOG.info(RETURNERER_KVITTERING, kvittering);
        return kvittering;
    }

    @Override
    public String ping() {
        return connection.ping();
    }

    private Vedlegg vedleggFra(TilbakebetalingUttalelse u) {
        var vedlegg = new Vedlegg();
        vedlegg.setBeskrivelse("Tekst fra bruker");
        vedlegg.setId(id());
        vedlegg.setContent(pdfGenerator.generate(u));
        vedlegg.setSkjemanummer(u.getBrukerTekst().getDokumentType());
        return vedlegg;
    }

    private static TilbakebetalingUttalelse uttalelseFra(Ettersending e) {
        return new TilbakebetalingUttalelse(e.getType(),
                e.getSaksnummer(),
                e.getDialogId(),
                e.getBrukerTekst());
    }

    private static String id() {
        return "V" + IDGENERATOR.nextLong();
    }

    private void hentOgSjekk(List<Vedlegg> vedlegg) {
        if (!vedlegg.isEmpty()) {
            LOG.info("Henter og sjekker mellomlagring for {} vedlegg", vedlegg.size());
            vedlegg.forEach(this::hentVedleggBytes);
            vedleggSjekker.sjekk(vedlegg.get(0));
            LOG.info("Hentet og sjekket mellomlagring OK for {} vedlegg", vedlegg.size());
        }
    }

    private void slettMellomlagringOgSøknad(Søknad søknad) {
        LOG.info("Sletter mellomlagret søknad og vedlegg");
        søknad.getVedlegg().forEach(mellomlagring::slettKryptertVedlegg);
        mellomlagring.slettKryptertSøknad();
        LOG.info("Slettet mellomlagret søknad og vedlegg OK");
    }

    private void hentVedleggBytes(Vedlegg vedlegg) {
        if (vedlegg.getUrl() != null) {
            vedlegg.setContent(mellomlagring.lesKryptertVedlegg(vedlegg.getUuid())
                    .map(a -> a.bytes)
                    .orElse(new byte[] {}));
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + ", mellomlagring=" + mellomlagring
                + ", vedleggSjekker=" + vedleggSjekker + "]";
    }

}
