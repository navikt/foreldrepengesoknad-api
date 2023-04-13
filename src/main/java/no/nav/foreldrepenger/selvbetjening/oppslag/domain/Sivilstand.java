package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

public record Sivilstand(Sivilstand.Type type) {
    public enum Type {
        UOPPGITT,
        UGIFT,
        GIFT,
        ENKE_ELLER_ENKEMANN,
        SKILT,
        SEPARERT,
        REGISTRERT_PARTNER,
        SEPARERT_PARTNER,
        SKILT_PARTNER,
        GJENLEVENDE_PARTNER,
    }
}
