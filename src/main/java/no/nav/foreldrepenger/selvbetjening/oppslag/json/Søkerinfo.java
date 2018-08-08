package no.nav.foreldrepenger.selvbetjening.oppslag.json;

import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.SøkerinfoDto;

import java.util.ArrayList;
import java.util.List;

public class Søkerinfo {

    public Person søker;
    public List<Arbeidsforhold> arbeidsforhold = new ArrayList<>();

    public Søkerinfo(SøkerinfoDto dto) {
        this.søker = new Person(dto.person);
        this.arbeidsforhold.addAll(dto.arbeidsforhold);
    }
}
