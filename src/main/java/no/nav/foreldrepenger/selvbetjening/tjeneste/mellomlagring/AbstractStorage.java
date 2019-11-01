package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStorage implements Storage {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractStorage.class);

    protected abstract boolean writeString(String bøtte, String katalog, String key, String value);

    protected abstract String readString(String bøtte, String katalog, String key);

    protected abstract boolean deleteString(String bøtte, String katalog, String key);

    public AbstractStorage(String søknadBøtte, String mellomlagringBøtte) {
        this.søknadBøtte = søknadBøtte;
        this.mellomlagringBøtte = mellomlagringBøtte;
    }

    private final String søknadBøtte;
    private final String mellomlagringBøtte;

    @Override
    public String ping() {
        put("ping", "pingKey", "42");
        delete("ping", "pingKey");
        return "OK";
    }

    @Override
    public void put(String katalog, String key, String value) {
        LOG.info("Lagrer søknad i bøtte {}, katalog {}", søknadBøtte, katalog);
        if (writeString(søknadBøtte, katalog, key, value)) {
            LOG.info("Lagret søknad OK i bøtte {}, katalog {}", søknadBøtte, katalog);
        } else {
            LOG.warn("Lagret ikke søknad i bøtte {}, katalog {}", søknadBøtte, katalog);
        }
    }

    @Override
    public void putTmp(String katalog, String key, String value) {
        LOG.info("Mellomlagrer søknad i bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        if (writeString(mellomlagringBøtte, katalog, key, value)) {
            LOG.info("Mellomlagret søknad OK i bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        } else {
            LOG.warn("Mellomlagret ikke søknad i bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        }
    }

    @Override
    public Optional<String> get(String katalog, String key) {
        LOG.info("Henter søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
        var søknad = Optional.ofNullable(readString(søknadBøtte, katalog, key));
        if (søknad.isPresent()) {
            LOG.info("Hentet søknad OK fra bøtte {}, katalog {}", søknadBøtte, katalog);
        } else {
            LOG.info("Hentet ingen søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
        }
        return søknad;
    }

    @Override
    public Optional<String> getTmp(String katalog, String key) {
        LOG.info("Henter mellomlagret søknad fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        var søknad = Optional.ofNullable(readString(mellomlagringBøtte, katalog, key));
        if (søknad.isPresent()) {
            LOG.info("Hentet mellomlagret søknad OK fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        } else {
            LOG.info("Hentet ingen mellomlagret søknad fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        }
        return søknad;
    }

    @Override
    public void delete(String katalog, String key) {
        LOG.info("Fjerner søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
        if (deleteString(søknadBøtte, katalog, key)) {
            LOG.info("Fjernet søknad OK fra bøtte {}, katalog {}", søknadBøtte, katalog);
        } else {
            LOG.warn("Fjernet ikke søknad fra bøtte {}, katalog {}", søknadBøtte, katalog);
        }
    }

    @Override
    public void deleteTmp(String katalog, String key) {
        LOG.info("Fjerner mellomlagret søknad fra bøtte {}, katalog {}", mellomlagringBøtte, katalog);
        deleteString(mellomlagringBøtte, katalog, key);
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
