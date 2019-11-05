package no.nav.foreldrepenger.selvbetjening.util.conditionals;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_SBS;

import org.springframework.core.type.AnnotatedTypeMetadata;

import no.nav.foreldrepenger.selvbetjening.util.Cluster;

class OnProdCondition extends OnClusterCondition {

    @Override
    protected Cluster[] clusters(AnnotatedTypeMetadata md) {
        return new Cluster[] { PROD_SBS, PROD_GCP };
    }
}