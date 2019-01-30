package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.SøkerinfoDto;

@JsonInclude(NON_EMPTY)
public class Søkerinfo {

    private final Person søker;
    private final List<Arbeidsforhold> arbeidsforhold;

    public Søkerinfo(SøkerinfoDto dto) {
        this.søker = new Person(dto.person);
        this.arbeidsforhold = Optional.ofNullable(dto.arbeidsforhold).orElse(emptyList());
    }

    public Person getSøker() {
        return søker;
    }

    public List<Arbeidsforhold> getArbeidsforhold() {
        return arbeidsforhold;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [søker=" + søker + ", arbeidsforhold=" + arbeidsforhold + "]";
    }
}
