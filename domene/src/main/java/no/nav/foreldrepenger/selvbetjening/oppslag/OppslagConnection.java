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
    private final OppslagOversiktConfig config2;

    public OppslagConnection(RestOperations operations, OppslagConfig config, OppslagOversiktConfig config2) {
        super(operations);
        this.config = config;
        this.config2 = config2;
    }

    public Person hentPerson() {
        var opprinneligFraMottak = getForObject(config.personURI(), Person.class);
        try {
            var oversikt = getForObject(config2.personURI(), Person.class);
            if (Objects.equals(oversikt, opprinneligFraMottak)) {
                LOG.info("PERSON: Likt resultat fra oversikt og mottak!");
            } else {
                LOG.info("PERSON: Ulikt resultat fra oversikt og mottak. Sjekk diff mottak: {} VS oversikt: {}", opprinneligFraMottak, oversikt);
            }
        } catch (Exception e) {
            LOG.info("PERSON: Noe feilet med kall nytt person endepunkt i oversikt!", e);
        }

        return opprinneligFraMottak;

    }

    public List<Arbeidsforhold> hentArbeidsForhold() {
        var arbeidsforholdGammel = Optional.ofNullable(getForObject(config.arbeidsforholdURI(), Arbeidsforhold[].class, false)).map(Arrays::asList).orElse(emptyList());

        try {
            var oversikt = Optional.ofNullable(getForObject(config2.arbeidsforholdURI(), Arbeidsforhold[].class, false)).map(Arrays::asList).orElse(emptyList());
            if (Objects.equals(oversikt, arbeidsforholdGammel)) {
                LOG.info("ARBEID: Likt resultat fra oversikt og mottak!");
            } else {
                LOG.info("ARBEID: Ulikt resultat fra oversikt og mottak. Sjekk diff mottak: {} VS oversikt: {}", arbeidsforholdGammel, oversikt);
            }
        } catch (Exception e) {
            LOG.info("ARBEID: Noe feilet med kall nytt person endepunkt i oversikt!", e);
        }

        return arbeidsforholdGammel;
    }



    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }
}
