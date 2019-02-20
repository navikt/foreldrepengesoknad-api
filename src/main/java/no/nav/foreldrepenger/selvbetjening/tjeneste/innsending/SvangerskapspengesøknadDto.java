package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Svangerskapspengesøknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto.YtelseDto;

import java.util.ArrayList;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.time.LocalDateTime.now;

@JsonInclude(NON_EMPTY)
public class SvangerskapspengesøknadDto extends SøknadDto {

    public YtelseDto ytelse;

    public SvangerskapspengesøknadDto(Svangerskapspengesøknad søknad) {
        this.søker = new SøkerDto("MOR");
        this.ytelse = new YtelseDto(søknad);
        this.vedlegg = new ArrayList<>();
        this.mottattdato = now();
    }
}
