package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.common.domain.felles.Person;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class OppslagConnection extends AbstractRestConnection {
    private final OppslagConfig config;

    public OppslagConnection(RestOperations operations, OppslagConfig config) {
        super(operations);
        this.config = config;
    }

    public Person hentPerson() {
        return getForObject(config.personURI(), Person.class);
    }

    public PersonMedArbeidsforholdDto søkerinfo() {
        return getForObject(config.personMedArbeidsforhold(), PersonMedArbeidsforholdDto.class);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }
}
