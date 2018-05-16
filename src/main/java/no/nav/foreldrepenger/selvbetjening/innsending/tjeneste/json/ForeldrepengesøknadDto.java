package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;

import java.util.ArrayList;

import static java.time.LocalDateTime.now;

public class ForeldrepengesøknadDto extends SøknadDto {

    public YtelseDto ytelse;

    public ForeldrepengesøknadDto(Foreldrepengesøknad søknad) {
        this.søker = new SøkerDto();
        this.ytelse = new YtelseDto(søknad.utenlandsopphold, søknad.barn, søknad.annenForelder);
        this.vedlegg = new ArrayList<>();
        this.mottattdato = now();
    }
}
