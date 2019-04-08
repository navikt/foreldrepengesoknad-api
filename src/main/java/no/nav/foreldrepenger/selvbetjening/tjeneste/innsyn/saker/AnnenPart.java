package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnenPart {
    private final String fnr;
    private final String aktørId;
    private final Navn navn;

    @JsonCreator
    public AnnenPart(@JsonProperty("fnr") String fnr, @JsonProperty("aktørId") String aktørId,
            @JsonProperty("navn") Navn navn) {
        this.fnr = fnr;
        this.aktørId = aktørId;
        this.navn = navn;
    }

    public String getFnr() {
        return fnr;
    }

    public String getAktørId() {
        return aktørId;
    }

    public Navn getNavn() {
        return navn;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fnr=" + fnr + ", aktørId=" + aktørId + ", navn=" + navn + "]";
    }
}
