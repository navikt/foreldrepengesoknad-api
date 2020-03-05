package no.nav.foreldrepenger.selvbetjening.config;

import static java.lang.System.getenv;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEFAULT;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEV_GCP;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEV_SBS;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.LOCAL;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAIS_CLUSTER_NAME;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterAwareSpringProfileResolver {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterAwareSpringProfileResolver.class);

    private ClusterAwareSpringProfileResolver() {

    }

    public static String[] profiles() {
        return Optional.ofNullable(profilFra(getenv(NAIS_CLUSTER_NAME)))
                .map(c -> new String[] { c })
                .orElse(new String[0]);
    }

    private static String profilFra(String cluster) {
        if (cluster == null) {
            LOG.info("NAIS cluster ikke detektert, antar {}", LOCAL);
            System.setProperty(NAIS_CLUSTER_NAME, LOCAL);
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
