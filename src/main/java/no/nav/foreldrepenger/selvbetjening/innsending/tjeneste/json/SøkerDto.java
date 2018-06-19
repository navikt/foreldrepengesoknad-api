package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søker;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class SøkerDto {

    public String søknadsRolle;

    public SøkerDto(Søker søker) {
        this.søknadsRolle = søker.rolle;
    }

    public SøkerDto(String søknadsRolle) {
        this.søknadsRolle = søknadsRolle;
    }

}
