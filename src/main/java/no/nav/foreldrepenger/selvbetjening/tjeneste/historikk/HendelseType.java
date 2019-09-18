package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

public enum HendelseType {
    VEDTAK,
    INNTEKTSMELDING,
    INITIELL_ENGANGSSTØNAD,
    INITIELL_FORELDREPENGER,
    INITIELL_SVANGERSKAPSPENGER,
    ETTERSENDING_FORELDREPENGER,
    ETTERSENDING_ENGANGSSTØNAD,
    ETTERSENDING_SVANGERSKAPSPENGER,
    ENDRING_FORELDREPENGER,
    ENDRING_SVANGERSKAPSPENGER,
    UKJENT;

    public static HendelseType tilHendelse(String hendelse) {
        try {
            return valueOf(hendelse);
        } catch (Exception e) {
            return UKJENT;
        }
    }
}
