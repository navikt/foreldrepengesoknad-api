package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

public enum Hendelse {
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

    public static Hendelse tilHendelse(String hendelse) {
        try {
            return valueOf(hendelse);
        } catch (Exception e) {
            return UKJENT;
        }
    }
}
