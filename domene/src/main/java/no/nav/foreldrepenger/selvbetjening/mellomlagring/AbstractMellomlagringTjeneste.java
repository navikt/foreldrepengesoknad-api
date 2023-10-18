package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

import java.util.Optional;

import jakarta.annotation.PostConstruct;

public abstract class AbstractMellomlagringTjeneste implements Mellomlagring {

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
        return doLes(bøtte.navn(), katalog, key);
    }

    private void lagreI(Bøtte bøtte, String katalog, String key, String value) {
        doLagre(bøtte.navn(), katalog, key, value);
    }

    private void slettFra(Bøtte bøtte, String katalog, String key) {
        doSlett(bøtte.navn(), katalog, key);
    }

    private void validerBøtter(Bøtte... bøtter) {
        safeStream(bøtter).forEach(this::validerBøtte);
    }

    protected static String key(String directory, String key) {
        return directory + "_" + key;
    }

}
