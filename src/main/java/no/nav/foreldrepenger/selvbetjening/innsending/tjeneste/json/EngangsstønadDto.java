package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;

import java.util.ArrayList;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.time.LocalDateTime.now;

@JsonInclude(NON_EMPTY)
public class EngangsstønadDto extends SøknadDto {

    public YtelseDto ytelse;

    public EngangsstønadDto() {}

    public EngangsstønadDto(Engangsstønad engangsstønad, PersonDto person) {
        this.søker = new SøkerDto();
        this.ytelse = new YtelseDto(engangsstønad.utenlandsopphold, engangsstønad.barn, engangsstønad.annenForelder);
        this.vedlegg = new ArrayList<>();
        this.mottattdato = now();
        this.søker.søknadsRolle = "MOR";

        // TODO: Mottak bør hente personinfo via fnr fra oidc token selv.
        this.søker.fornavn = person.fornavn;
        this.søker.mellomnavn = person.mellomnavn;
        this.søker.etternavn = person.etternavn;
        this.søker.fnr = person.fnr;
        this.søker.aktør = person.aktorId;


    }
}
