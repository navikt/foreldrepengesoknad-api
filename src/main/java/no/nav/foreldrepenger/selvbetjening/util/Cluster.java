package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAIS_CLUSTER_NAME;

import java.util.Optional;

import org.springframework.core.env.Environment;

public enum Cluster {
    LOCAL(EnvUtil.LOCAL),
    DEV_SBS(EnvUtil.DEV_SBS),
    DEV_GCP(EnvUtil.DEV_GCP),
    PROD_GCP(EnvUtil.PROD_GCP),
    PROD_SBS(EnvUtil.PROD_SBS);

    private final String clusterName;

    Cluster(String clusterName) {
        this.clusterName = clusterName;
    }

    public String clusterName() {
        return clusterName;
    }

    public boolean isActive(Environment env) {
        return Optional.ofNullable(env.getProperty(NAIS_CLUSTER_NAME))
                .filter(clusterName::equals)
                .isPresent();
    }
}
