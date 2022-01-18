package no.nav.foreldrepenger.selvbetjening.innsyn.saker;

import static no.nav.foreldrepenger.common.util.StringUtil.partialMask;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
        return getClass().getSimpleName() + " [fnr=" + partialMask(fnr) + ", aktørId=" + aktørId + ", navn="
                + navn + "]";
    }
}
