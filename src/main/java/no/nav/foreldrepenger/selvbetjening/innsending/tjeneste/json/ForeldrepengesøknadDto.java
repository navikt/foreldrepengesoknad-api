package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Foreldrepengesøknad;

import java.util.ArrayList;

import static java.time.LocalDateTime.now;

public class ForeldrepengesøknadDto extends SøknadDto {

    public YtelseDto ytelse;

    public ForeldrepengesøknadDto(Foreldrepengesøknad søknad) {
        this.søker = new SøkerDto(søknad.søker);
        this.ytelse = new YtelseDto(søknad.type, søknad.utenlandsopphold, søknad.barn, søknad.annenForelder, søknad.uttaksplan);
        this.vedlegg = new ArrayList<>();

        this.mottattdato = now();
    }
}
