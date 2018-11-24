package no.nav.foreldrepenger.selvbetjening.health;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.InnsynConnection;

@Component
public class InnsynHealthIndicator extends AbstractEnvironmentAwareHealthIndicator {

    public InnsynHealthIndicator(InnsynConnection connection) {
        super(connection);
    }
}
