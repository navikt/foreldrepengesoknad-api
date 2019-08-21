package no.nav.foreldrepenger.selvbetjening.util;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

public final class EnvUtil {

    private static final String NOT = "!";
    public static final String TEST = "test";
    public static final String DEV = "dev";
    public static final String DEV_GCP = "dev-gcp";

    public static final String LOCAL = "local";
    public static final String LOCALSTACK = "localstack";
    public static final String NOTLOCALSTACK = NOT + LOCALSTACK;
    public static final String NOTLOCAL = NOT + LOCAL;
    public static final String DEFAULT = "default";

    public static final Marker CONFIDENTIAL = MarkerFactory.getMarker("CONFIDENTIAL");

    private EnvUtil() {

    }

    public static boolean isDevOrLocal(Environment env) {
        return isLocal(env) || isDev(env);
    }

    private static boolean isDev(Environment env) {
        return env.acceptsProfiles(Profiles.of(DEV, DEV_GCP));
    }

    private static boolean isLocal(Environment env) {
        return env == null || env.acceptsProfiles(Profiles.of(LOCAL));
    }
}
