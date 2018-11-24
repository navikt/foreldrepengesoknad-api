package no.nav.foreldrepenger.selvbetjening.felles.health;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn.InnsynConnection;

@Component
public class MottakHelthIndicator extends EnvironmentAwareHealthIndicator {

    public MottakHelthIndicator(InnsynConnection connection) {
        super(connection);
    }
}
