package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.util.StreamUtil.safeStream;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMellomlagringTjeneste implements Mellomlagring {
    private static final String DEAKIVERT = "Mellomlagringsoperasjoner er deaktivert";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMellomlagringTjeneste.class);

    protected abstract void validerBøtte(Bøtte bøtte);

    protected abstract void doStore(String bøtte, String katalog, String key, String value);

    protected abstract String doRead(String bøtte, String katalog, String key);

    protected abstract void doDelete(String bøtte, String katalog, String key);

    private final Bøtte søknadBøtte;
    private final Bøtte mellomlagringBøtte;

    public AbstractMellomlagringTjeneste(Bøtte søknadBøtte, Bøtte mellomlagringBøtte) {
        this.søknadBøtte = søknadBøtte;
        this.mellomlagringBøtte = mellomlagringBøtte;
    }

    @Override
    public void lagre(String katalog, String key, String value) {
        lagreI(søknadBøtte, katalog, key, value);
    }

    @Override
    public void lagreTmp(String katalog, String key, String value) {
        lagreI(mellomlagringBøtte, katalog, key, value);
    }

    @Override
    public Optional<String> les(String katalog, String key) {
        return lesFra(søknadBøtte, katalog, key);
    }

    @Override
    public Optional<String> lesTmp(String katalog, String key) {
        return lesFra(mellomlagringBøtte, katalog, key);
    }

    @Override
    public void slett(String katalog, String key) {
        slettFra(søknadBøtte, katalog, key);
    }

    @Override
    public void slettTmp(String katalog, String key) {
        slettFra(mellomlagringBøtte, katalog, key);
    }

    private Optional<String> lesFra(Bøtte bøtte, String katalog, String key) {
        try {
            if (bøtte.isEnabled()) {
                LOG.info("Henter fra bøtte {}, katalog {}", bøtte, katalog);
                var søknad = Optional.ofNullable(doRead(bøtte.getNavn(), katalog, key));
                if (søknad.isPresent()) {
                    LOG.info("Hentet fra bøtte {}, katalog {}", bøtte, katalog);
                } else {
                    LOG.info("Hentet ikke fra bøtte {}, katalog {}", bøtte, katalog);
                }
                return søknad;
            } else {
                return disabled();
            }
        } catch (MellomlagringException e) {
            LOG.warn("Hentet ikke fra bøtte {}, katalog {}", bøtte, katalog, e);
            return Optional.empty();
        }
    }

    private void lagreI(Bøtte bøtte, String katalog, String key, String value) {
        try {
            if (bøtte.isEnabled()) {
                LOG.info("Lagrer i bøtte {}, katalog {}", bøtte, katalog);
                doStore(bøtte.getNavn(), katalog, key, value);
                LOG.info("Lagret i bøtte {}, katalog {}", bøtte, katalog);
            } else {
                disabled();
            }
        } catch (MellomlagringException e) {
            LOG.warn("Lagret ikke i bøtte {}, katalog {}", bøtte, katalog, e);
        }
    }

    private void slettFra(Bøtte bøtte, String katalog, String key) {
        try {
            if (bøtte.isEnabled()) {
                LOG.info("Fjerner fra bøtte {}, katalog {}", bøtte, katalog);
                doDelete(bøtte.getNavn(), katalog, key);
                LOG.info("Fjerner fra bøtte {}, katalog {}", bøtte, katalog);
            } else {
                disabled();
            }
        } catch (MellomlagringException e) {
            LOG.warn("Fjernet ikke fra bøtte {}, katalog {}", bøtte, katalog, e);
        }
    }

    private void validerBøtter(Bøtte... bøtter) {
        try {
            safeStream(bøtter)
                    .forEach(this::validerBøtte);
        } catch (MellomlagringException e) {
            LOG.warn("{}", e.getMessage());
            // throw new MellomlagringException(e);
        }
    }

    Bøtte getSøknadBøtte() {
        return søknadBøtte;
    }

    Bøtte getMellomlagringBøtte() {
        return mellomlagringBøtte;
    }

    private static Optional<String> disabled() {
        LOG.warn(DEAKIVERT);
        return Optional.empty();
    }

    @PostConstruct
    void valider() {
        validerBøtter(søknadBøtte, mellomlagringBøtte);
    }

    @Override
    public String ping() {
        return ping("ping", "key", "42");
    }

    @Override
    public String name() {
        return pingURI().getHost();
    }

    private String ping(String ping, String key, String value) {
        lagre(ping, key, value);
        slett(ping, key);
        return "OK";
    }

    protected static String key(String directory, String key) {
        return directory + "_" + key;
    }

}
