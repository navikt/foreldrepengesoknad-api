package no.nav.foreldrepenger.selvbetjening.health;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.Innsyn;

@Component
public class InnsynHealthIndicator extends AbstractEnvironmentAwareHealthIndicator {

    public InnsynHealthIndicator(Innsyn tjeneste) {
        super(tjeneste);
    }
}
