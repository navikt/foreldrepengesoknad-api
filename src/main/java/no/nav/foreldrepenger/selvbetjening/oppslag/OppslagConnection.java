package no.nav.foreldrepenger.selvbetjening.oppslag;

import static java.util.Collections.emptyList;
import static no.nav.boot.conditionals.EnvUtil.CONFIDENTIAL;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.domain.felles.Person;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Arbeidsforhold;

@Component
public class OppslagConnection extends AbstractRestConnection {

    private final OppslagConfig config;

    public OppslagConnection(RestOperations operations, OppslagConfig config) {
        super(operations);
        this.config = config;
    }

    public Person hentPerson() {
        var person = getForObject(config.personURI(), Person.class);
        LOG.info(CONFIDENTIAL, "Fikk person {}", person);
        return person;

    }

    public List<Arbeidsforhold> hentArbeidsForhold() {
        return Optional.ofNullable(getForObject(config.arbeidsforholdURI(), Arbeidsforhold[].class, false)).map(Arrays::asList).orElse(emptyList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }
}
