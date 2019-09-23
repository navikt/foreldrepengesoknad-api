package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AvslagsÅrsak {
    AVSLAG;

    private static final Logger LOG = LoggerFactory.getLogger(AvslagsÅrsak.class);

    public static AvslagsÅrsak valueSafelyOf(String name) {
        try {
            return AvslagsÅrsak.valueOf(name);
        } catch (Exception e) {
            LOG.warn("Ingen enum verdi for {}", name);
            return null;
        }
    }

}
