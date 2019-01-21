package no.nav.foreldrepenger.selvbetjening.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Enabled {

    public static boolean TPSBARN;

    public Enabled(
            @Value("${TOGGLES_ENABLE_TPSBARN:false}") boolean tpsBarn) {
        Enabled.TPSBARN = tpsBarn;
    }

}
