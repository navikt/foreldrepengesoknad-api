package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto;

import static java.time.LocalDate.now;

import java.util.ArrayList;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Foreldrepengesøknad;

public class ForeldrepengesøknadDto extends SøknadDto {

    public String saksnr;
    public YtelseDto ytelse;

    public ForeldrepengesøknadDto(Foreldrepengesøknad søknad) {
        this.saksnr = søknad.saksnummer;
        this.søker = new SøkerDto(søknad.søker.rolle);
        this.ytelse = new YtelseDto(søknad);
        this.vedlegg = new ArrayList<>();
        this.mottattdato = now();
    }

}
