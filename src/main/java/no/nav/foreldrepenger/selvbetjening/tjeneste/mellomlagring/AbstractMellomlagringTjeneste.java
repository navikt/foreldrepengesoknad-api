package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMellomlagringTjeneste implements MellomlagringTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMellomlagringTjeneste.class);

    protected abstract boolean lagre(String bøtte, String katalog, String key, String value);

    protected abstract String les(String bøtte, String katalog, String key);

    protected abstract boolean slett(String bøtte, String katalog, String key);

    private final String søknadBøtte;
    private final String mellomlagringBøtte;

    public AbstractMellomlagringTjeneste(String søknadBøtte, String mellomlagringBøtte) {
        this.søknadBøtte = søknadBøtte;
        this.mellomlagringBøtte = mellomlagringBøtte;
    }

    @Override
    public String ping() {
        lagre("ping", "pingKey", "42");
        slett("ping", "pingKey");
        return "OK";
    }

    @Override
    public void lagre(String katalog, String key, String value) {
        LOG.info("Lagrer søknad i bøtte {}, katalog {}", søknadBøtte, katalog);
        if (lagre(søknadBøtte, katalog, key, value)) {
            LOG.info("Lagret søknad OK i bøtte {}, katalog {}", søknadBøtte, katalog);
        } else {
            LOG.warn("Lagret ikke søknad i bøtte {}, katalog {}", søknadBøtte, katalog);
        }
    }

    @Override
    public void lagreTmp(String katalog, String key, String value) {
        LOG.info("Mellomlagrer søknad i bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        if (lagre(mellomlagringBøtte, katalog, key, value)) {
            LOG.info("Mellomlagret søknad OK i bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        } else {
            LOG.warn("Mellomlagret ikke søknad i bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        }
    }

    @Override
    public Optional<String> les(String katalog, String key) {
        LOG.info("Henter søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
        var søknad = Optional.ofNullable(les(søknadBøtte, katalog, key));
        if (søknad.isPresent()) {
            LOG.info("Hentet søknad OK fra bøtte {}, katalog {}", søknadBøtte, katalog);
        } else {
            LOG.info("Hentet ingen søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
        }
        return søknad;
    }

    @Override
    public Optional<String> lesTmp(String katalog, String key) {
        LOG.info("Henter mellomlagret søknad fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        var søknad = Optional.ofNullable(les(mellomlagringBøtte, katalog, key));
        if (søknad.isPresent()) {
            LOG.info("Hentet mellomlagret søknad OK fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        } else {
            LOG.info("Hentet ingen mellomlagret søknad fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        }
        return søknad;
    }

    @Override
    public void slett(String katalog, String key) {
        LOG.info("Fjerner søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
        if (slett(søknadBøtte, katalog, key)) {
            LOG.info("Fjernet søknad OK fra bøtte {}, katalog {}", søknadBøtte, katalog);
        } else {
            LOG.warn("Fjernet ikke søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
        }
    }

    @Override
    public void slettTmp(String katalog, String key) {
        LOG.info("Fjerner mellomlagret søknad fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        slett(mellomlagringBøtte, katalog, key);
        LOG.info("Fjerner mellomlagret søknad OK fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
    }

    String getSøknadBøtte() {
        return søknadBøtte;
    }

    String getMellomlagringBøtte() {
        return mellomlagringBøtte;
    }

    protected static String fileName(String directory, String key) {
        return directory + "_" + key;
    }

}
