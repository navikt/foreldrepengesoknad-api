package no.nav.foreldrepenger.selvbetjening.util;

import static org.springframework.boot.autoconfigure.condition.ConditionMessage.forCondition;
import static org.springframework.boot.autoconfigure.condition.ConditionOutcome.match;
import static org.springframework.boot.autoconfigure.condition.ConditionOutcome.noMatch;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnClusterCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnCluster.class.getName());
        Cluster cluster = (Cluster) attributes.get("cluster");
        return getMatchOutcome(context.getEnvironment(), cluster);
    }

    private ConditionOutcome getMatchOutcome(Environment env, Cluster cluster) {
        ConditionMessage.Builder message = forCondition(ConditionalOnCluster.class);
        if (cluster == null) {
            return noMatch(message.didNotFind("cluster").atAll());
        }
        String name = cluster.name();
        if (cluster.isActive(env)) {
            return match(message.foundExactly(name));
        }
        return noMatch(message.didNotFind(name).atAll());
    }

}
