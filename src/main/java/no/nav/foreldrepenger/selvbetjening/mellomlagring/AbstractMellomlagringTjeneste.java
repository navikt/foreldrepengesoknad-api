package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMellomlagringTjeneste implements Mellomlagring {
    private static final String DEAKIVERT = "Mellomlagringsoperasjoner er deaktivert";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMellomlagringTjeneste.class);

    protected abstract void validerBøtte(Bøtte bøtte);

    protected abstract void doLagre(String bøtte, String katalog, String key, String value);

    protected abstract Optional<String> doLes(String bøtte, String katalog, String key);

    protected abstract void doSlett(String bøtte, String katalog, String key);

    private final Bøtte mellomlagringBøtte;

    protected AbstractMellomlagringTjeneste(Bøtte mellomlagringBøtte) {
        this.mellomlagringBøtte = mellomlagringBøtte;
    }

    @PostConstruct
    void valider() {
        validerBøtter(mellomlagringBøtte);
    }

    @Override
    public boolean isEnabled() {
        return mellomlagringBøtte.isEnabled();
    }

    @Override
    public void lagre(MellomlagringType type, String katalog, String key, String value) {
        lagreI(mellomlagringBøtte, katalog, key, value);
    }

    @Override
    public Optional<String> les(MellomlagringType type, String katalog, String key) {
        return lesFra(mellomlagringBøtte, katalog, key);
    }

    @Override
    public void slett(MellomlagringType type, String katalog, String key) {
        slettFra(mellomlagringBøtte, katalog, key);
    }

    private Optional<String> lesFra(Bøtte bøtte, String katalog, String key) {
        if (bøtte.isEnabled()) {
            return doLes(bøtte.getNavn(), katalog, key);
        }
        return disabled();
    }

    private void lagreI(Bøtte bøtte, String katalog, String key, String value) {
        if (bøtte.isEnabled()) {
            doLagre(bøtte.getNavn(), katalog, key, value);
        } else {
            disabled();
        }
    }

    private void slettFra(Bøtte bøtte, String katalog, String key) {
        if (bøtte.isEnabled()) {
            doSlett(bøtte.getNavn(), katalog, key);
        } else {
            disabled();
        }
    }

    private void validerBøtter(Bøtte... bøtter) {
        safeStream(bøtter)
                .filter(Bøtte::isEnabled)
                .forEach(this::validerBøtte);
    }

    private static Optional<String> disabled() {
        LOG.warn(DEAKIVERT);
        return Optional.empty();
    }

    @Override
    public String ping() {
        return ping(MellomlagringType.KORTTIDS, "ping", "key", "42");
    }

    @Override
    public String name() {
        return pingURI().getHost();
    }

    private String ping(MellomlagringType type, String ping, String key, String value) {
        lagre(type, ping, key, value);
        slett(type, ping, key);
        return "OK";
    }

    protected static String key(String directory, String key) {
        return directory + "_" + key;
    }

}
