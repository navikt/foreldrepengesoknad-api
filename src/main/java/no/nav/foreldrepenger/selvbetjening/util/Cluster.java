package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.NAIS_CLUSTER_NAME;

import java.util.Optional;

import org.springframework.core.env.Environment;

public enum Cluster {
    LOCAL(EnvUtil.LOCAL),
    DEV(EnvUtil.DEV),
    DEV_GCP(EnvUtil.DEV_GCP),
    PROD_GCP(EnvUtil.PROD_GCP),
    PROD(EnvUtil.PROD);

    private final String name;

    Cluster(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isActive(Environment env) {
        return Optional.ofNullable(env.getProperty(NAIS_CLUSTER_NAME))
                .filter(name::equals)
                .isPresent();
    }
}
