package no.nav.foreldrepenger.selvbetjening.util;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

public final class EnvUtil {

    public static final String PREPROD = "preprod";
    public static final String DEV = "dev";
    public static final Marker CONFIDENTIAL = MarkerFactory.getMarker("CONFIDENTIAL");

    private EnvUtil() {

    }

    public static boolean isDevOrPreprod(Environment env) {
        return env == null || isDev(env) || isPreprod(env);
    }

    public static boolean isPreprod(Environment env) {
        return env.acceptsProfiles(Profiles.of(PREPROD));
    }

    public static boolean isDev(Environment env) {
        return env.acceptsProfiles(Profiles.of(DEV));
    }

    public static boolean isProd(Environment env) {
        return !isDevOrPreprod(env);
    }
}
