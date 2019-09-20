package no.nav.foreldrepenger.selvbetjening.config;

import static java.lang.System.getenv;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEFAULT;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.LOCAL;

public class ClusterAwareSpringProfileResolver {

    private static final String NAIS_CLUSTER_NAME = "NAIS_CLUSTER_NAME";

    private ClusterAwareSpringProfileResolver() {

    }

    public static String[] getProfiles() {
        return new String[] { clusterFra(getenv(NAIS_CLUSTER_NAME)) };
    }

    private static String clusterFra(String cluster) {
        if (cluster == null) {
            return LOCAL;
        }
        if (cluster.contains(DEV)) {
            return DEV;
        }
        return DEFAULT;
    }
}
