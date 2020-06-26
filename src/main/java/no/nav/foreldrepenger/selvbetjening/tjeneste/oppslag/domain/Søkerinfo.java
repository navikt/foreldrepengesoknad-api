package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public class Søkerinfo {

    private final Person søker;
    private final List<Arbeidsforhold> arbeidsforhold;

    public Søkerinfo(Person søker, List<Arbeidsforhold> arbeidsforhold) {
        this.søker = søker;
        this.arbeidsforhold = arbeidsforhold;
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
