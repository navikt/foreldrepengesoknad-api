package no.nav.foreldrepenger.selvbetjening.felles.health;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn.InnsynConnection;

@Component
public class InnsynHealthIndicator extends AbstractEnvironmentAwareHealthIndicator {

    public InnsynHealthIndicator(InnsynConnection connection) {
        super(connection);
    }
}
