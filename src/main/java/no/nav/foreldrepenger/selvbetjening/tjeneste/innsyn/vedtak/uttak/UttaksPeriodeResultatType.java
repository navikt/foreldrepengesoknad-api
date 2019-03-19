package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum UttaksPeriodeResultatType {
    INNVILGET, AVSLÅTT;

    private static final Logger LOG = LoggerFactory.getLogger(UttaksPeriodeResultatType.class);

    public static UttaksPeriodeResultatType valueSafelyOf(String name) {
        try {
            return UttaksPeriodeResultatType.valueOf(name);
        } catch (Exception e) {
            LOG.warn("Ingen enum verdi for {}", name);
            return null;
        }
    }
}
