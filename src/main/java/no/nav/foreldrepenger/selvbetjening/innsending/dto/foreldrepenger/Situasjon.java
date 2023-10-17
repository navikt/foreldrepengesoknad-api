package no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Situasjon {
    FØDSEL("fødsel"),
    ADOPSJON("adopsjon"),
    OMSORGSOVERTAKELSE("omsorgsovertakelse");

    private String kode;
    Situasjon(String kode) {
        this.kode = kode;
    }

    @JsonValue
    public String kode() { // TODO sjekk at deseralisering/seraliseirng gjør ut i fra kode!
        return kode;
    }
}
