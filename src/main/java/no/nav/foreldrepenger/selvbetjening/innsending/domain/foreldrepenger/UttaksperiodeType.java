package no.nav.foreldrepenger.selvbetjening.innsending.domain.foreldrepenger;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UttaksperiodeType {
    UTTAK("uttak"),
    UTSETTELSE("utsettelse"),
    OPPHOLD("opphold"),
    OVERFØRING("overføring"),
    PERIODE_UTEN_UTTAK("periodeUtenUttak");

    private final String verdi;

    UttaksperiodeType(String verdi) {
        this.verdi = verdi;
    }

    @JsonValue
    public String verdi() {
        return verdi;
    }
}
