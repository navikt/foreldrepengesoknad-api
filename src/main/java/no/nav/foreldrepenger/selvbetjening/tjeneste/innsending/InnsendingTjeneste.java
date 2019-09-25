package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.MellomlagringTjeneste;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
@ConditionalOnProperty(name = "stub.mottak", havingValue = "false", matchIfMissing = true)
public class InnsendingTjeneste implements Innsending {

    private static final Logger LOG = getLogger(InnsendingTjeneste.class);

    private final InnsendingConnection connection;
    private final MellomlagringTjeneste mellomlagring;
    private final VedleggSjekker vedleggSjekker;

    public InnsendingTjeneste(InnsendingConnection connection, MellomlagringTjeneste mellomlagring,
            VedleggSjekker vedleggSjekker) {
        this.connection = connection;
        this.mellomlagring = mellomlagring;
        this.vedleggSjekker = vedleggSjekker;
    }

    @Override
    public Kvittering sendInn(Søknad søknad) {
        LOG.info("Sender inn søknad av type {}", søknad.getType());
        hentOgSjekk(søknad.getVedlegg());
        Kvittering kvittering = connection.sendInn(søknad);
        slettMellomlagringOgSøknad(søknad);
        LOG.info("Returnerer kvittering {}", kvittering);
        return kvittering;
    }

    @Override
    public Kvittering ettersend(Ettersending ettersending) {
        LOG.info("Ettersender for sak {}", ettersending.getSaksnummer());
        hentOgSjekk(ettersending.getVedlegg());
        Kvittering kvittering = connection.ettersend(ettersending);
        ettersending.getVedlegg().forEach(v -> mellomlagring.slettVedlegg(v));
        LOG.info("Returnerer kvittering {}", kvittering);
        return kvittering;
    }

    @Override
    public Kvittering endre(Søknad endringssøknad) {
        LOG.info("Endrer søknad av type {}", endringssøknad.getType());
        hentOgSjekk(endringssøknad.getVedlegg());
        Kvittering kvittering = connection.endre(endringssøknad);
        slettMellomlagringOgSøknad(endringssøknad);
        LOG.info("Returnerer kvittering {}", kvittering);
        return kvittering;
    }

    @Override
    public String ping() {
        return connection.ping();
    }

    @Override
    public URI pingURI() {
        return connection.pingURI();
    }

    private void hentOgSjekk(List<Vedlegg> vedlegg) {
        LOG.info("Henter og sjekker mellomlagring for {} vedlegg", vedlegg.size());
        vedlegg.forEach(this::hentVedleggBytes);
        vedleggSjekker.sjekk(vedlegg);
        LOG.info("Hentet og sjekket mellomlagring OK for {} vedlegg", vedlegg.size());

    }

    private void slettMellomlagringOgSøknad(Søknad søknad) {
        LOG.info("Sletter mellomlagret søknad og vedlegg");
        søknad.getVedlegg().forEach(v -> mellomlagring.slettVedlegg(v));
        mellomlagring.slettSøknad();
        LOG.info("Slettet mellomlagret søknad og vedlegg OK");
    }

    private void hentVedleggBytes(Vedlegg vedlegg) {
        if (vedlegg.getUrl() != null) {
            vedlegg.setContent(mellomlagring.hentVedlegg(vedlegg.getUuid())
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
