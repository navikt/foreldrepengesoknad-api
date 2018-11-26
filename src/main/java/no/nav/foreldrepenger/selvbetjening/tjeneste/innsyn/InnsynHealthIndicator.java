package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.health.AbstractEnvironmentAwareHealthIndicator;

@Component
public class InnsynHealthIndicator extends AbstractEnvironmentAwareHealthIndicator {

    public InnsynHealthIndicator(Innsyn tjeneste) {
        super(tjeneste);
    }
}
