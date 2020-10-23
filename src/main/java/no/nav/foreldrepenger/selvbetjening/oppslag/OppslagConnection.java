package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.foreldrepenger.selvbetjening.util.MDCUtil.CONFIDENTIAL;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.PersonDto;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.SøkerinfoDto;

@Component
public class OppslagConnection extends AbstractRestConnection {

    private final OppslagConfig config;

    public OppslagConnection(RestOperations operations, OppslagConfig config) {
        super(operations);
        this.config = config;
    }

    boolean isUsePdl() {
        return config.isUsePdl();
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    public PersonDto hentPDLPerson() {
        LOG.warn("Henter PDL-person fra {}", config.personPDLURI());
        return getForObject(config.personPDLURI(), PersonDto.class);
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

    public SøkerinfoDto hentSøkerInfo() {
        SøkerinfoDto info = getForObject(config.søkerInfoURI(), SøkerinfoDto.class);
        LOG.info(CONFIDENTIAL, "Fikk søkerinfo {}", info);
        return info;

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
