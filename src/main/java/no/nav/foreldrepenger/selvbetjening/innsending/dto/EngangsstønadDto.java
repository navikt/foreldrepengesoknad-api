package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Engangsstønad;

@JsonInclude(NON_EMPTY)
public class EngangsstønadDto extends SøknadDto {

    public YtelseDto ytelse;

    public EngangsstønadDto(Engangsstønad søknad) {
        this.søker = new SøkerDto("MOR");
        this.ytelse = new YtelseDto(søknad);
        this.vedlegg = new ArrayList<>();
    }
}
