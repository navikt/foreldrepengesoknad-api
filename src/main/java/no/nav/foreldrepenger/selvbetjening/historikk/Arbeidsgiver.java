package no.nav.foreldrepenger.selvbetjening.historikk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Arbeidsgiver {
    private final String navn;
    private final String orgnr;

    @JsonCreator
    public Arbeidsgiver(@JsonProperty("navn") String navn, @JsonProperty("orgnr") String orgnr) {
        this.navn = navn;
        this.orgnr = orgnr;
    }

    public String getNavn() {
        return navn;
    }

    public String getOrgnr() {
        return orgnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[navn=" + navn + ", orgnr=" + orgnr + "]";
    }
}
