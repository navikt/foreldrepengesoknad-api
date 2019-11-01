package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_SBS;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_SBS;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractPingableHealthIndicator;
import no.nav.foreldrepenger.selvbetjening.util.ConditionalOnClusters;

@Component
@ConditionalOnClusters(clusters = { DEV_GCP, PROD_GCP, DEV_SBS, PROD_SBS })
public class MellomlagringHealthIndicator extends AbstractPingableHealthIndicator {

    public MellomlagringHealthIndicator(Storage pingable) {
        super(pingable);
    }
}
