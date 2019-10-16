package no.nav.foreldrepenger.selvbetjening.config;

import static java.lang.System.getenv;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAIS_CLUSTER_NAME;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEFAULT;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.DEV_SBS;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.LOCAL;

import java.util.Optional;

public class ClusterAwareSpringProfileResolver {

    private ClusterAwareSpringProfileResolver() {

    }

    public static String[] profiles() {
        return Optional.ofNullable(clusterFra(getenv(NAIS_CLUSTER_NAME)))
                .map(c -> new String[] { c })
                .orElse(new String[0]);
    }

    private static String clusterFra(String cluster) {
        if (cluster == null) {
            return LOCAL;
        }
        if (cluster.equals(DEV_SBS)) {
            return DEV_SBS;
        }
        if (cluster.equals(DEV_GCP)) {
            return DEV_GCP;
        }
        return DEFAULT;
    }
}
