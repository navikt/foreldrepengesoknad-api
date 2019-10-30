package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.CONFIDENTIAL;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.PersonDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.SøkerinfoDto;

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

    public PersonDto hentPerson() {
        PersonDto person = getForObject(config.getPersonURI(), PersonDto.class);
        LOG.info(CONFIDENTIAL, "Fikk person {}", person);
        return person;

    }

    public SøkerinfoDto hentSøkerInfo() {
        SøkerinfoDto info = getForObject(config.getSøkerinfoURI(), SøkerinfoDto.class);
        LOG.info(CONFIDENTIAL, "Fikk søkerinfo {}", info);
        return info;

    }

    public AktørId HentAktørId(String fnr) {
        return getForObject(config.getAktørIdURI(fnr), AktørId.class);
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
