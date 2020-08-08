package no.nav.foreldrepenger.selvbetjening.config;

import static java.lang.System.getenv;
import static java.lang.System.setProperty;
import static no.nav.foreldrepenger.boot.conditionals.Cluster.NAIS_CLUSTER_NAME;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEV;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEV_FSS;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEV_GCP;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEV_SBS;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.LOCAL;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.PROD;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.PROD_FSS;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.PROD_GCP;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.PROD_SBS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterAwareSpringProfileResolver {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterAwareSpringProfileResolver.class);

    public static String[] profiler() {
        return profilerFraCluster(getenv(NAIS_CLUSTER_NAME));
    }

    private static String[] profilerFraCluster(String cluster) {
        if (cluster == null) {
            LOG.info("NAIS cluster ikke detektert, antar {}", LOCAL);
            setProperty(NAIS_CLUSTER_NAME, LOCAL);
            return new String[] { LOCAL };
        }
        if (cluster.equals(DEV_SBS)) {
            return new String[] { DEV, DEV_SBS };
        }
        if (cluster.equals(DEV_GCP)) {
            return new String[] { DEV, DEV_GCP };
        }
        if (cluster.equals(PROD_GCP)) {
            return new String[] { PROD, PROD_GCP };
        }
        if (cluster.equals(PROD_SBS)) {
            return new String[] { PROD, PROD_SBS };
        }
        if (cluster.equals(DEV_FSS)) {
            return new String[] { DEV, DEV_FSS };
        }
        if (cluster.equals(PROD_FSS)) {
            return new String[] { DEV, PROD_FSS };
        }
        throw new IllegalArgumentException("Cluster " + cluster + " er ikke støttet");
    }
}
