package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json.PersonDto;

import java.util.ArrayList;

import static java.time.LocalDateTime.now;

public class ForeldrepengesøknadDto extends SøknadDto {

    public YtelseDto ytelse;
    public OpptjeningDto opptjening;
    public FordelingDto fordeling;

    public ForeldrepengesøknadDto(Foreldrepengesøknad søknad) {
        this.søker = new SøkerDto(søknad.søkerRolle);
        this.ytelse = new YtelseDto(søknad.type, søknad.utenlandsopphold, søknad.barn, søknad.annenForelder);
        this.vedlegg = new ArrayList<>();
        this.opptjening = new OpptjeningDto();
        this.fordeling = new FordelingDto();

        this.mottattdato = now();
    }
}
