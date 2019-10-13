package no.nav.foreldrepenger.selvbetjening.util;

import static no.nav.foreldrepenger.selvbetjening.util.StreamUtil.safeStream;
import static org.springframework.boot.autoconfigure.condition.ConditionMessage.forCondition;
import static org.springframework.boot.autoconfigure.condition.ConditionOutcome.match;
import static org.springframework.boot.autoconfigure.condition.ConditionOutcome.noMatch;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ClusterCondition extends SpringBootCondition {
    private static final Logger LOG = LoggerFactory.getLogger(ClusterCondition.class);

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var message = forCondition(ConditionalOnClusters.class);
        var clusters = Cluster[].class
                .cast(metadata.getAnnotationAttributes(ConditionalOnClusters.class.getName()).get("clusters"));

        return safeStream(clusters)
                .filter(cluster -> cluster.isActive(context.getEnvironment()))
                .map(cluster -> match(message.foundExactly(cluster.clusterName())))
                .findFirst()
                .orElseGet(() -> noMatch(message.because(Arrays.toString(clusters))));
    }

}
