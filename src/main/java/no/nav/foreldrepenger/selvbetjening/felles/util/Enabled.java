package no.nav.foreldrepenger.selvbetjening.felles.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Enabled {

    public static boolean foreldrepengesøknad;

    public Enabled(@Value("${toggles.enable.foreldrepengesoknad:false}") boolean foreldrepengesøknad) {
        Enabled.foreldrepengesøknad = foreldrepengesøknad;
    }

}
