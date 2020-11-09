package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.foreldrepenger.selvbetjening.util.MDCUtil.CONFIDENTIAL;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.PersonDto;

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

    public PersonDto hentPDLPerson() {
        LOG.warn("Henter PDL-person fra {}", config.personURI());
        return getForObject(config.personURI(), PersonDto.class);
    }

    public PersonDto hentPerson() {
        if (isEnabled()) {
            PersonDto person = getForObject(config.personURI(), PersonDto.class);
            LOG.info(CONFIDENTIAL, "Fikk person {}", person);
            return person;
        }
        LOG.warn("Oppslag av person er deaktivert");
        return null;

    }

    public AktørId HentAktørId(String fnr) {
        return getForObject(config.aktørIdURI(fnr), AktørId.class);
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
