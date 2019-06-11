package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Svangerskapspengesøknad;

@JsonInclude(NON_EMPTY)
public class SvangerskapspengesøknadDto extends SøknadDto {

    public YtelseDto ytelse;

    public SvangerskapspengesøknadDto(Svangerskapspengesøknad søknad) {
        this.søker = new SøkerDto("MOR", søknad.søker.språkkode);
        this.ytelse = new YtelseDto(søknad);
        this.vedlegg = new ArrayList<>();
    }
}
