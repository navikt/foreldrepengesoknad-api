package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto;

import java.util.ArrayList;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Foreldrepengesøknad;

public class ForeldrepengesøknadDto extends SøknadDto {

    public String saksnr;
    public YtelseDto ytelse;

    public ForeldrepengesøknadDto(Foreldrepengesøknad søknad) {
        this.saksnr = søknad.getSaksnummer();
        this.søker = new SøkerDto(søknad.getSøker().rolle, søknad.getSøker().språkkode);
        this.ytelse = new YtelseDto(søknad);
        this.vedlegg = new ArrayList<>();
    }

}
