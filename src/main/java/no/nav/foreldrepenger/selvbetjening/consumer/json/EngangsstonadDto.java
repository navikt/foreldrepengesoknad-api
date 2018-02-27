package no.nav.foreldrepenger.selvbetjening.consumer.json;

import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstonad;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EngangsstonadDto {

    public LocalDateTime mottattdato;

    public SøkerDto søker;
    public YtelseDto ytelse;

    public String begrunnelseForSenSøknad;
    public String tilleggsopplysninger;

    public EngangsstonadDto() {}

    public EngangsstonadDto(Engangsstonad engangsstonad) {
        this.søker = new SøkerDto();
        this.ytelse = new YtelseDto();

        this.søker.fornavn = "Per";
        this.søker.etternavn = "Fugelli";
        this.søker.fnr = "01010199999";
        this.søker.aktør = "aktør-id";
        this.søker.søknadsRolle = "MOR";

        this.ytelse.type = "engangsstønad";

        this.mottattdato = LocalDateTime.now();

        this.ytelse.medlemsskap.norgeSiste12 = true;
        this.ytelse.medlemsskap.norgeNeste12 = true;
        this.ytelse.medlemsskap.arbeidSiste12 = "ARBEIDET_I_NORGE";
        this.ytelse.medlemsskap.fødselNorge = true;

        this.ytelse.relasjonTilBarn.type = "termin";
        this.ytelse.relasjonTilBarn.antallBarn = 1;
        this.ytelse.relasjonTilBarn.terminDato = LocalDate.now().plusMonths(5);
        this.ytelse.relasjonTilBarn.utstedtDato = LocalDate.now().minusDays(5);

    }
}
