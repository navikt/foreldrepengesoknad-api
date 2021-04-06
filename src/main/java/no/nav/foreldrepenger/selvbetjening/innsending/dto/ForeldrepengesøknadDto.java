package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.util.ArrayList;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Foreldrepengesøknad;

public class ForeldrepengesøknadDto extends SøknadDto {

    public String saksnr;
    public YtelseDto ytelse;

    public ForeldrepengesøknadDto(Foreldrepengesøknad søknad) {
        this.saksnr = søknad.getSaksnummer();
        this.søker = new SøkerDto(søknad.getSøker().getRolle(), søknad.getSøker().getSpråkkode());
        this.ytelse = new YtelseDto(søknad);
        this.vedlegg = new ArrayList<>();
    }

}
