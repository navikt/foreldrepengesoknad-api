package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Engangsstønad;

import java.util.ArrayList;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.time.LocalDateTime.now;

@JsonInclude(NON_EMPTY)
public class EngangsstønadDto extends SøknadDto {

    public YtelseDto ytelse;

    public EngangsstønadDto(Engangsstønad søknad) {
        this.søker = new SøkerDto("MOR");
        this.ytelse = new YtelseDto(søknad.type, søknad.utenlandsopphold, søknad.barn, søknad.annenForelder);
        this.vedlegg = new ArrayList<>();
        this.mottattdato = now();

    }
}
