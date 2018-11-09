package no.nav.foreldrepenger.selvbetjening.felles.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Enabled {

    public static boolean endringssøknad;

    public Enabled(
            @Value("${TOGGLES_ENABLE_ENDRINGSSOKNAD:false}") boolean endringssøknad
    ) {
        Enabled.endringssøknad = endringssøknad;
    }

}
