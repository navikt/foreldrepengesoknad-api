package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak;

public enum MorsAktivitet {
    ARBEID, UTDANNING, KVALPROG("morsaktivitet.kvalprog"), INTROPROG("morsaktivitet.introprog"),
    TRENGER_HJELP("morsaktivitet.sykdom"), INNLAGT, ARBEID_OG_UTDANNING,
    SAMTIDIGUTTAK("morsaktivitet.samtidig"), UFØRE;

    private final String key;

    public String getKey() {
        return key;
    }

    MorsAktivitet() {
        this(null);
    }

    MorsAktivitet(String key) {
        this.key = key;
    }
}
