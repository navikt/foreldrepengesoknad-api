package no.nav.foreldrepenger.selvbetjening.innsyn.saker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

public class AnnenPart {
    private final String fnr;
    private final AktørId aktørId;
    private final Navn navn;

    @JsonCreator
    public AnnenPart(@JsonProperty("fnr") String fnr, @JsonProperty("aktørId") AktørId aktørId,
            @JsonProperty("navn") Navn navn) {
        this.fnr = fnr;
        this.aktørId = aktørId;
        this.navn = navn;
    }

    public String getFnr() {
        return fnr;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public Navn getNavn() {
        return navn;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fnr=" + StringUtil.maskFnr(fnr) + ", aktørId=" + aktørId + ", navn="
                + navn + "]";
    }
}
