package no.nav.foreldrepenger.selvbetjening.util.conditionals;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.LOCAL;

import org.springframework.core.type.AnnotatedTypeMetadata;

import no.nav.foreldrepenger.selvbetjening.util.Cluster;

class OnLocalCondition extends OnClusterCondition {

    @Override
    protected Cluster[] clusters(AnnotatedTypeMetadata md) {
        return new Cluster[] { LOCAL };
    }
}