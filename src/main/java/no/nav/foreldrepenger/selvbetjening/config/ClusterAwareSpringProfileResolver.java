package no.nav.foreldrepenger.selvbetjening.config;

import static java.lang.System.getenv;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEV_GCP;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEV_SBS;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.LOCAL;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.PROD_GCP;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.PROD_SBS;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAIS_CLUSTER_NAME;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterAwareSpringProfileResolver {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterAwareSpringProfileResolver.class);

    private ClusterAwareSpringProfileResolver() {

    }

    public static String[] profiles() {
        return Optional.ofNullable(getenv(NAIS_CLUSTER_NAME))
                .map(ClusterAwareSpringProfileResolver::checkIfLocal)
                .map(c -> new String[] { c })
                .orElse(new String[0]);
    }

    private static String checkIfLocal(String cluster) {
        return switch (cluster) {
            case PROD_SBS -> PROD_SBS;
            case PROD_GCP -> PROD_GCP;
            case DEV_SBS -> DEV_SBS;
            case DEV_GCP -> DEV_GCP;
            default -> {
                LOG.info("NAIS cluster ikke detektert, antar {}", LOCAL);
                System.setProperty(NAIS_CLUSTER_NAME, LOCAL);
                yield LOCAL;
            }
        };
    }
}
