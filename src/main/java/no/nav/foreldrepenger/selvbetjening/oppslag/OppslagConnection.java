package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.CONFIDENTIAL;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.domain.felles.Person;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;

@Component
public class OppslagConnection extends AbstractRestConnection {

    private final OppslagConfig config;

    public OppslagConnection(RestOperations operations, OppslagConfig config) {
        super(operations);
        this.config = config;
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    public Person hentPerson() {
        if (isEnabled()) {
            var person = getForObject(config.personURI(), Person.class);
            LOG.info(CONFIDENTIAL, "Fikk person {}", person);
            return person;
        }
        LOG.warn("Oppslag av person er deaktivert");
        return null;

    }

    @Override
    public URI pingURI() {
        return config.pingURI();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }
}
