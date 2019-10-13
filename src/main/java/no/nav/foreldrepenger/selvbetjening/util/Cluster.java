package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAIS_CLUSTER_NAME;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

public enum Cluster {
    LOCAL(EnvUtil.LOCAL),
    DEV(EnvUtil.DEV),
    DEV_GCP(EnvUtil.DEV_GCP),
    PROD_GCP(EnvUtil.PROD_GCP),
    PROD(EnvUtil.PROD);

    private final String name;
    private static final Logger LOG = LoggerFactory.getLogger(Cluster.class);

    Cluster(String name) {
        this.name = name;
    }

    public String clusterName() {
        return name;
    }

    public boolean isActive(Environment env) {
        LOG.info("Checking if {} is active", this);
        boolean active = Optional.ofNullable(env.getProperty(NAIS_CLUSTER_NAME))
                .filter(name::equals)
                .isPresent();
        LOG.info("{} is " + (active ? "" : "not ") + "active", this);
        return active;
    }
}
