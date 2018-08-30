package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import static java.time.LocalDateTime.now;

import java.util.ArrayList;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Foreldrepengesøknad;

public class ForeldrepengesøknadDto extends SøknadDto {

    public YtelseDto ytelse;

    public ForeldrepengesøknadDto(Foreldrepengesøknad søknad) {
        this.søker = new SøkerDto(søknad.søker.rolle);
        this.ytelse = new YtelseDto(søknad);
        this.vedlegg = new ArrayList<>();
        this.mottattdato = now();
    }

}
