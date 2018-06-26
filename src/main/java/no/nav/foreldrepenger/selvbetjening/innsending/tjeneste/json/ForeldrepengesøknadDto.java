package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import static java.time.LocalDateTime.now;

import java.util.ArrayList;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Foreldrepengesøknad;

public class ForeldrepengesøknadDto extends SøknadDto {

    public YtelseDto ytelse;

    public ForeldrepengesøknadDto(Foreldrepengesøknad søknad) {
        this.søker = new SøkerDto(søknad.søker);
        this.ytelse = new YtelseDto(søknad.type, søknad.informasjonOmUtenlandsopphold, søknad.barn,
                søknad.annenForelder,
                søknad.uttaksplan);
        this.vedlegg = new ArrayList<>();
        this.mottattdato = now();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [ytelse=" + ytelse + "]";
    }
}
