package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static org.slf4j.LoggerFactory.getLogger;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.BrukerTekst;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.pdf.PDFGenerator;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.KryptertMellomlagringTjeneste;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "false", matchIfMissing = true)
public class InnsendingTjeneste implements Innsending {

    private static final String RETURNERER_KVITTERING = "Returnerer kvittering {}";
    private static final Logger LOG = getLogger(InnsendingTjeneste.class);
    private static final Random IDGENERATOR = new SecureRandom();
    private final InnsendingConnection connection;
    private final KryptertMellomlagringTjeneste mellomlagring;
    private final VedleggSjekker vedleggSjekker;
    private final PDFGenerator pdfGenerator;

    public InnsendingTjeneste(InnsendingConnection connection, KryptertMellomlagringTjeneste mellomlagring,
            VedleggSjekker vedleggSjekker, PDFGenerator pdfGenerator) {
        this.connection = connection;
        this.mellomlagring = mellomlagring;
        this.vedleggSjekker = vedleggSjekker;
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    public Kvittering sendInn(Søknad søknad) {
        LOG.info("Sender inn søknad av type {}", søknad.getType());
        hentOgSjekk(søknad.getVedlegg());
        Kvittering kvittering = connection.sendInn(søknad);
        slettMellomlagringOgSøknad(søknad);
        LOG.info(RETURNERER_KVITTERING, kvittering);
        return kvittering;
    }

    @Override
    public Kvittering ettersend(Ettersending ettersending) {
        LOG.info("Ettersender for sak {}", ettersending.getSaksnummer());
        hentOgSjekk(ettersending.getVedlegg());
        if (ettersending.getBrukerTekst() != null) {
            LOG.info("Konverterer tekst til vedleggs-pdf {}", ettersending.getBrukerTekst().getDokumentType());
            ettersending.getVedlegg().add(vedleggFra(ettersending.getBrukerTekst()));
        }
        Kvittering kvittering = connection.ettersend(ettersending);
        ettersending.getVedlegg().forEach(mellomlagring::slettKryptertVedlegg);
        LOG.info(RETURNERER_KVITTERING, kvittering);
        return kvittering;
    }

    @Override
    public Kvittering endre(Søknad endringssøknad) {
        LOG.info("Endrer søknad av type {}", endringssøknad.getType());
        hentOgSjekk(endringssøknad.getVedlegg());
        Kvittering kvittering = connection.endre(endringssøknad);
        slettMellomlagringOgSøknad(endringssøknad);
        LOG.info(RETURNERER_KVITTERING, kvittering);
        return kvittering;
    }

    @Override
    public boolean isEnabled() {
        return connection.isEnabled();
    }

    @Override
    public String ping() {
        return connection.ping();
    }

    private Vedlegg vedleggFra(BrukerTekst brukerTekst) {
        Vedlegg vedlegg = new Vedlegg();
        vedlegg.setBeskrivelse("Tekst fra bruker");
        vedlegg.setId(id());
        vedlegg.setContent(pdfGenerator.generate(brukerTekst.getOverskrift(), brukerTekst.getTekst()));
        vedlegg.setSkjemanummer(brukerTekst.getDokumentType());
        return vedlegg;
    }

    private static String id() {
        return "V" + IDGENERATOR.nextLong();
    }

    private void hentOgSjekk(List<Vedlegg> vedlegg) {
        if (!vedlegg.isEmpty()) {
            LOG.info("Henter og sjekker mellomlagring for {} vedlegg", vedlegg.size());
            vedlegg.forEach(this::hentVedleggBytes);
            vedleggSjekker.sjekk(vedlegg);
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
