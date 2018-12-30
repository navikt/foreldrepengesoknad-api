package no.nav.foreldrepenger.selvbetjening.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Enabled {

    public static boolean ENDRINGSSØKNAD;
    public static boolean FPSAKSAKER;
    public static boolean TPSBARN;

    public Enabled(
            @Value("${TOGGLES_ENABLE_ENDRINGSSOKNAD:false}") boolean endringssøknad,
            @Value("${TOGGLES_ENABLE_FPSAKSAKER:false}") boolean fpsakSaker,
            @Value("${TOGGLES_ENABLE_TPSBARN:false}") boolean tpsBarn
    ) {
        Enabled.ENDRINGSSØKNAD = endringssøknad;
        Enabled.FPSAKSAKER = fpsakSaker;
        Enabled.TPSBARN = tpsBarn;
    }

}
