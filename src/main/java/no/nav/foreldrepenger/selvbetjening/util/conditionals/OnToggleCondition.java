package no.nav.foreldrepenger.selvbetjening.util.conditionals;

import static org.springframework.boot.autoconfigure.condition.ConditionMessage.forCondition;
import static org.springframework.boot.autoconfigure.condition.ConditionOutcome.match;
import static org.springframework.boot.autoconfigure.condition.ConditionOutcome.noMatch;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import no.finn.unleash.DefaultUnleash;
import no.finn.unleash.Unleash;
import no.finn.unleash.util.UnleashConfig;

public class OnToggleCondition extends SpringBootCondition {

    private final Unleash unleash;

    public OnToggleCondition() {
        this.unleash = unleash();
    }

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext ctx, AnnotatedTypeMetadata metadata) {
        var message = forCondition(ConditionalOnToggle.class);
        var toggle = toggle(metadata);
        return unleash.isEnabled(toggle) ? match(message.foundExactly(toggle))
                : noMatch(message.because("Toggle " + "ikke satt"));
    }

    private static String toggle(AnnotatedTypeMetadata md) {
        return String.class.cast(md.getAnnotationAttributes(ConditionalOnToggle.class.getName()).get("value"));
    }

    public static Unleash unleash() {
        return new DefaultUnleash(UnleashConfig.builder()
                .appName("foreldrepengesoknad-api")
                .instanceId("foreldrepengesoknad-api-instance")
                .unleashAPI("https://unleash.nais.adeo.no/api/")
                .build());
    }
}
