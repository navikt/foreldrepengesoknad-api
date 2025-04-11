package no.nav.foreldrepenger.selvbetjening.oppslag;

import no.nav.foreldrepenger.common.domain.felles.Person;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Arbeidsforhold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Component
public class OppslagConnection extends AbstractRestConnection {
    private static final Logger LOG = LoggerFactory.getLogger(OppslagConnection.class);
    private final OppslagConfig config;
    private final OppslagOversiktConfig configMotOverikt;

    public OppslagConnection(RestOperations operations, OppslagConfig config, OppslagOversiktConfig configMotOverikt) {
        super(operations);
        this.config = config;
        this.configMotOverikt = configMotOverikt;
    }

    public Person hentPerson() {
        return getForObject(config.personURI(), Person.class);
    }

    public List<Arbeidsforhold> hentArbeidsForhold() {
        return Optional.ofNullable(getForObject(config.arbeidsforholdURI(), Arbeidsforhold[].class, false)).map(Arrays::asList).orElse(emptyList());
    }

    public void sammenlignMedNyttEndepunktIOversikt(Person orginal) {
        try {
            var oversikt = getForObject(configMotOverikt.personURI(), Person.class);
            if (Objects.equals(orginal, oversikt)) {
                LOG.info("PERSON: Likt resultat fra oversikt og mottak!");
            } else {
                LOG.info("PERSON: Ulikt resultat fra oversikt og mottak. Sjekk diff mottak: {} VS oversikt: {}", orginal, oversikt);
            }
        } catch (Exception e) {
            LOG.info("PERSON: Noe feilet med kall nytt person endepunkt i oversikt!", e);
        }
    }

    public void sammenlignResultatNyttKallIOversikt(Person personDto, List<Arbeidsforhold> arbeidsforhold) {
        try {
            var original = new PersonMedArbeidsforholdDto(personDto, arbeidsforhold);
            var søkerinfo = getForObject(configMotOverikt.personMedArbeidsforhold(), PersonMedArbeidsforholdDto.class);
            if (Objects.equals(original, søkerinfo)) {
                LOG.info("SØKERINFO: Likt resultat fra oversikt og mottak!");
            } else {
                LOG.info("SØKERINFO: Ulikt resultat fra oversikt og mottak. Sjekk diff mottak: {} VS oversikt: {}", original, søkerinfo);
            }
        } catch (Exception e) {
            LOG.info("SØKERINFO: Noe feilet med kall nytt person endepunkt i oversikt!", e);
        }
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
