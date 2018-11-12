package no.nav.foreldrepenger.selvbetjening.felles.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Enabled {

    public static boolean ENDRINGSSØKNAD;
    public static boolean FPSAKSAKER;

    public Enabled(
            @Value("${TOGGLES_ENABLE_ENDRINGSSOKNAD:false}") boolean endringssøknad,
            @Value("${TOGGLES_ENABLE_FPSAKSAKER:false}") boolean fpsakSaker
    ) {
        Enabled.ENDRINGSSØKNAD = endringssøknad;
        Enabled.FPSAKSAKER = fpsakSaker;
    }

}
