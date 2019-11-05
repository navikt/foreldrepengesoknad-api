package no.nav.foreldrepenger.selvbetjening.util.conditionals;

import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.DEV_SBS;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.LOCAL;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_GCP;
import static no.nav.foreldrepenger.selvbetjening.util.Cluster.PROD_SBS;
import static no.nav.foreldrepenger.selvbetjening.util.StreamUtil.safeStream;
import static org.springframework.boot.autoconfigure.condition.ConditionMessage.forCondition;
import static org.springframework.boot.autoconfigure.condition.ConditionOutcome.match;
import static org.springframework.boot.autoconfigure.condition.ConditionOutcome.noMatch;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import no.nav.foreldrepenger.selvbetjening.util.Cluster;

public class OnClusterCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var message = forCondition(ConditionalOnClusters.class);
        var clusters = clusters(metadata);
        return safeStream(clusters)
                .filter(cluster -> cluster.isActive(context.getEnvironment()))
                .map(cluster -> match(message.foundExactly(cluster.clusterName())))
                .findFirst()
                .orElseGet(() -> noMatch(message.because(Arrays.toString(clusters))));
    }

    protected Cluster[] clusters(AnnotatedTypeMetadata metadata) {
        return Cluster[].class
                .cast(metadata.getAnnotationAttributes(ConditionalOnClusters.class.getName()).get("clusters"));
    }

    class OnLocalCondition extends OnClusterCondition {

        @Override
        protected Cluster[] clusters(AnnotatedTypeMetadata metadata) {
            return new Cluster[] { LOCAL };
        }
    }

    class OnGCPCondition extends OnClusterCondition {

        @Override
        protected Cluster[] clusters(AnnotatedTypeMetadata metadata) {
            return new Cluster[] { DEV_GCP, PROD_GCP };
        }
    }

    public class OnProdCondition extends OnClusterCondition {

        @Override
        protected Cluster[] clusters(AnnotatedTypeMetadata metadata) {
            return new Cluster[] { PROD_SBS, PROD_GCP };
        }
    }

    class OnSBSCondition extends OnClusterCondition {

        @Override
        protected Cluster[] clusters(AnnotatedTypeMetadata metadata) {
            return new Cluster[] { DEV_SBS, PROD_SBS };
        }
    }

    class OnDevCondition extends OnClusterCondition {

        @Override
        protected Cluster[] clusters(AnnotatedTypeMetadata metadata) {
            return new Cluster[] { DEV_SBS, DEV_GCP };
        }
    }

}
