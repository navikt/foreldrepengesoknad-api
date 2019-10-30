package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;
import no.nav.foreldrepenger.selvbetjening.util.Cluster;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnClusters;

@Component
@ConditionalOnClusters(clusters = { Cluster.DEV_GCP, Cluster.PROD_GCP, Cluster.DEV_SBS, Cluster.PROD_SBS })
public class MellomlagringHealthIndicator extends AbstractPingableHealthIndicator {

    public MellomlagringHealthIndicator(Storage pingable) {
        super(pingable);
    }
}
