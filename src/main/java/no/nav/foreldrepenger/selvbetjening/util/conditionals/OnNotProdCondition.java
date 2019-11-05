package no.nav.foreldrepenger.selvbetjening.util.conditionals;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_SBS;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.LOCAL;

import org.springframework.core.type.AnnotatedTypeMetadata;

import no.nav.foreldrepenger.selvbetjening.util.Cluster;

class OnNotProdCondition extends OnClusterCondition {

    @Override
    protected Cluster[] clusters(AnnotatedTypeMetadata md) {
        return new Cluster[] { DEV_SBS, DEV_GCP, LOCAL };
    }
}