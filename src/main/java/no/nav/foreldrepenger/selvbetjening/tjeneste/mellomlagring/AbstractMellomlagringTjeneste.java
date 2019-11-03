package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMellomlagringTjeneste implements Mellomlagring {
    private static final String DEAKIVERT = "Mellomlagringsoperasjoner er deaktivert";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMellomlagringTjeneste.class);

    protected abstract void lagre(String bøtte, String katalog, String key, String value);

    protected abstract String les(String bøtte, String katalog, String key);

    protected abstract void slett(String bøtte, String katalog, String key);

    private final String søknadBøtte;
    private final String mellomlagringBøtte;
    private final boolean enabled;

    public AbstractMellomlagringTjeneste(String søknadBøtte, String mellomlagringBøtte, boolean enabled) {
        this.søknadBøtte = søknadBøtte;
        this.mellomlagringBøtte = mellomlagringBøtte;
        this.enabled = enabled;
    }

    @Override
    public String ping() {
        lagre("ping", "pingKey", "42");
        slett("ping", "pingKey");
        return "OK";
    }

    @Override
    public void lagre(String katalog, String key, String value) {
        if (isEnabled()) {
            try {
                LOG.info("Lagrer søknad i bøtte {}, katalog {}", søknadBøtte, katalog);
                lagre(søknadBøtte, katalog, key, value);
                LOG.info("Lagret søknad OK i bøtte {}, katalog {}", søknadBøtte, katalog);
            } catch (MellomlagringException e) {
                LOG.warn("Lagret ikke søknad i bøtte {}, katalog {}", søknadBøtte, katalog);
            }
        } else {
            LOG.warn(DEAKIVERT);
        }
    }

    @Override
    public void lagreTmp(String katalog, String key, String value) {
        if (isEnabled()) {
            try {
                LOG.info("Mellomlagrer søknad i bøtte {}, katalog {}", mellomlagringBøtte, katalog);
                lagre(mellomlagringBøtte, katalog, key, value);
                LOG.info("Mellomlagret søknad OK i bøtte {}, katalog {}", mellomlagringBøtte, katalog);
            } catch (MellomlagringException e) {
                LOG.warn("Mellomlagret ikke søknad i bøtte {}, katalog {}", mellomlagringBøtte, katalog);
            }
        } else {
            LOG.warn(DEAKIVERT);
        }
    }

    @Override
    public Optional<String> les(String katalog, String key) {
        if (isEnabled()) {
            LOG.info("Henter søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
            var søknad = Optional.ofNullable(les(søknadBøtte, katalog, key));
            if (søknad.isPresent()) {
                LOG.info("Hentet søknad OK fra bøtte {}, katalog {}", søknadBøtte, katalog);
            } else {
                LOG.info("Hentet ingen søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
            }
            return søknad;
        } else {
            LOG.warn(DEAKIVERT);
            return null;
        }
    }

    @Override
    public Optional<String> lesTmp(String katalog, String key) {
        if (isEnabled()) {
            LOG.info("Henter mellomlagret søknad fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
            var søknad = Optional.ofNullable(les(mellomlagringBøtte, katalog, key));
            if (søknad.isPresent()) {
                LOG.info("Hentet mellomlagret søknad OK fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
            } else {
                LOG.info("Hentet ingen mellomlagret søknad fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
            }
            return søknad;
        } else {
            LOG.warn(DEAKIVERT);
            return null;
        }
    }

    @Override
    public void slett(String katalog, String key) {
        if (isEnabled()) {
            try {
                LOG.info("Fjerner søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
                slett(søknadBøtte, katalog, key);
                LOG.info("Fjernet søknad OK fra bøtte {}, katalog {}", søknadBøtte, katalog);
            } catch (MellomlagringException e) {
                LOG.warn("Fjernet ikke søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
            }
        } else {
            LOG.warn(DEAKIVERT);
        }
    }

    @Override
    public void slettTmp(String katalog, String key) {
        if (isEnabled()) {
            LOG.info("Fjerner mellomlagret søknad fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
            slett(mellomlagringBøtte, katalog, key);
            LOG.info("Fjerner mellomlagret søknad OK fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        } else {
            LOG.warn(DEAKIVERT);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    String getSøknadBøtte() {
        return søknadBøtte;
    }

    String getMellomlagringBøtte() {
        return mellomlagringBøtte;
    }

    protected static String key(String directory, String key) {
        return directory + "_" + key;
    }

}
