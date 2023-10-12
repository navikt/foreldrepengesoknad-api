package no.nav.foreldrepenger.selvbetjening.innsending.dto.ettersendelse;

import com.fasterxml.jackson.annotation.JsonValue;

public enum YtelseType {
    FORELDREPENGER("foreldrepenger"),
    SVANGERSKAPSPENGER("svangerskapspenger"),
    ENGANGSSTØNAD("engangsstønad");

    private final String verdi;

    YtelseType(String verdi) {
        this.verdi = verdi;
    }

    @JsonValue
    public String verdi() {
        return verdi;
    }
}
