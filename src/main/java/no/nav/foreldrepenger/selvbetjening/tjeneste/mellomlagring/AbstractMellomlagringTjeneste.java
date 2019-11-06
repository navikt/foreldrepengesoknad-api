package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.util.StreamUtil.safeStream;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.selvbetjening.error.UnexpectedInputException;

public abstract class AbstractMellomlagringTjeneste implements Mellomlagring {
    private static final String MSG = "{} bøtte {}, katalog {}";
    private static final String DEAKIVERT = "Mellomlagringsoperasjoner er deaktivert";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMellomlagringTjeneste.class);

    protected abstract void validerBøtte(Bøtte bøtte);

    protected abstract void doLagre(String bøtte, String katalog, String key, String value);

    protected abstract String doLes(String bøtte, String katalog, String key);

    protected abstract void doSlett(String bøtte, String katalog, String key);

    private final Bøtte søknadBøtte;
    private final Bøtte mellomlagringBøtte;

    public AbstractMellomlagringTjeneste(Bøtte søknadBøtte, Bøtte mellomlagringBøtte) {
        this.søknadBøtte = søknadBøtte;
        this.mellomlagringBøtte = mellomlagringBøtte;
    }

    @PostConstruct
    void valider() {
        validerBøtter(søknadBøtte, mellomlagringBøtte);
    }

    @Override
    public void lagre(MellomlagringType type, String katalog, String key, String value) {
        lagreI(bøtteFor(type), katalog, key, value);
    }

    @Override
    public Optional<String> les(MellomlagringType type, String katalog, String key) {
        return lesFra(bøtteFor(type), katalog, key);
    }

    @Override
    public void slett(MellomlagringType type, String katalog, String key) {
        slettFra(bøtteFor(type), katalog, key);
    }

    private Optional<String> lesFra(Bøtte bøtte, String katalog, String key) {
        try {
            if (bøtte.isEnabled()) {
                LOG.info(MSG, "Henter fra", bøtte, katalog);
                var søknad = Optional.ofNullable(doLes(bøtte.getNavn(), katalog, key));
                if (søknad.isPresent()) {
                    LOG.info(MSG, "Hentet fra", bøtte, katalog);
                } else {
                    LOG.info(MSG, "Hentet ikke fra", bøtte, katalog);
                }
                return søknad;
            } else {
                return disabled();
            }
        } catch (MellomlagringException e) {
            LOG.warn(MSG, "(feil) Hentet ikke", bøtte, katalog, e);
            return Optional.empty();
        }
    }

    private void lagreI(Bøtte bøtte, String katalog, String key, String value) {
        try {
            if (bøtte.isEnabled()) {
                LOG.info(MSG, "Lagrer i", bøtte, katalog);
                doLagre(bøtte.getNavn(), katalog, key, value);
                LOG.info(MSG, "Lagret i", bøtte, katalog);
            } else {
                disabled();
            }
        } catch (MellomlagringException e) {
            LOG.warn(MSG, "(feil) Lagret ikke i", bøtte, katalog, e);
        }
    }

    private void slettFra(Bøtte bøtte, String katalog, String key) {
        try {
            if (bøtte.isEnabled()) {
                LOG.info(MSG, "Fjerner fra", bøtte, katalog);
                doSlett(bøtte.getNavn(), katalog, key);
                LOG.info(MSG, "Fjernet fra", bøtte, katalog);
            } else {
                disabled();
            }
        } catch (MellomlagringException e) {
            LOG.warn(MSG, "(feil) Fjernet ikke fra", bøtte, katalog, e);
        }
    }

    private void validerBøtter(Bøtte... bøtter) {
        try {
            safeStream(bøtter)
                    .filter(Bøtte::isEnabled)
                    .forEach(this::validerBøtte);
        } catch (MellomlagringException e) {
            LOG.warn("{}", e.getMessage());
            // throw new MellomlagringException(e);
        }
    }

    private Bøtte bøtteFor(MellomlagringType type) {
        switch (type) {
        case LANGTIDS:
            return søknadBøtte;
        case KORTTIDS:
            return mellomlagringBøtte;
        default:
            throw new UnexpectedInputException("Type " + type + " er ukjent");
        }
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
