package no.nav.foreldrepenger.selvbetjening.innsending.json;

import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json.EngangsstønadDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Engangsstønad extends Søknad {

    public Barn barn;
    public Utenlandsopphold utenlandsopphold;
    public AnnenForelder annenForelder;

    public Engangsstønad() {
        this.barn = new Barn();
        this.utenlandsopphold = new Utenlandsopphold();
        this.annenForelder = new AnnenForelder();
    }

    public Engangsstønad(EngangsstønadDto dto) {
        this.opprettet = dto.mottattdato;
    }

    public static Engangsstønad stub() {
        Engangsstønad engangsstønad = new Engangsstønad();
        engangsstønad.opprettet = LocalDateTime.now();
        engangsstønad.sistEndret = LocalDateTime.now();

        engangsstønad.barn.erBarnetFødt = false;
        engangsstønad.barn.termindato = LocalDate.of(2018, 1, 31);
        engangsstønad.barn.terminbekreftelseDato = LocalDate.of(2017, 11, 24);
        engangsstønad.barn.antallBarn = 1;

        engangsstønad.utenlandsopphold.iNorgeSiste12Mnd = true;
        engangsstønad.utenlandsopphold.iNorgeNeste12Mnd = true;
        engangsstønad.utenlandsopphold.fødselINorge = true;

        return engangsstønad;
    }
}
