package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Engangsstønad;

import java.util.ArrayList;

import static java.time.LocalDateTime.now;

public class EngangsstønadDto extends SøknadDto {

    public YtelseDto ytelse;

    public EngangsstønadDto(Engangsstønad søknad) {
        this.søker = new SøkerDto("MOR");
        this.ytelse = new YtelseDto(søknad);
        this.vedlegg = new ArrayList<>();
        this.mottattdato = now();

    }
}
