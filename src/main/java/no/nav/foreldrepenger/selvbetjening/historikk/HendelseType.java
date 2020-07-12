package no.nav.foreldrepenger.selvbetjening.historikk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum HendelseType {
    TILBAKEKREVING_SPM,
    TILBAKEKREVING_SVAR,
    TILBAKEKREVING_FATTET_VEDTAK,
    TILBAKEKREVING_SPM_LUKKET,
    TILBAKEKREVING_HENLAGT,
    VEDTAK,
    INNTEKTSMELDING,
    INNTEKTSMELDING_ENDRING,
    INITIELL_ENGANGSSTØNAD,
    INITIELL_FORELDREPENGER,
    INITIELL_SVANGERSKAPSPENGER,
    ETTERSENDING_FORELDREPENGER,
    ETTERSENDING_ENGANGSSTØNAD,
    ETTERSENDING_SVANGERSKAPSPENGER,
    ENDRING_FORELDREPENGER,
    ENDRING_SVANGERSKAPSPENGER,
    UKJENT;

    private static final Logger LOG = LoggerFactory.getLogger(HendelseType.class);

    public static HendelseType tilHendelse(String hendelse) {
        try {
            return valueOf(hendelse);
        } catch (Exception e) {
            LOG.warn("Kunne ikke oversette {} til hendelse, returnerer UKJENT", hendelse);
            return UKJENT;
        }
    }
}
