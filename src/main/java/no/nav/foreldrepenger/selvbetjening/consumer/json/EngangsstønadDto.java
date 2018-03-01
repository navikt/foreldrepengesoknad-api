package no.nav.foreldrepenger.selvbetjening.consumer.json;

import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstønad;

import java.time.LocalDateTime;

public class EngangsstønadDto {

    public LocalDateTime mottattdato;

    public SøkerDto søker;
    public YtelseDto ytelse;

    public EngangsstønadDto() {}

    public EngangsstønadDto(Engangsstønad engangsstønad) {
        this.søker = new SøkerDto();
        this.ytelse = new YtelseDto();

        this.søker.fornavn = "Mor";
        this.søker.etternavn = "Fugelli";
        this.søker.fnr = "01010199999";
        this.søker.aktør = "aktør-id";
        this.søker.søknadsRolle = "MOR";

        this.ytelse.type = "engangsstønad";

        this.mottattdato = LocalDateTime.now();

        this.ytelse.medlemsskap.norgeSiste12 = engangsstønad.utenlandsopphold.iNorgeSiste12Mnd;
        this.ytelse.medlemsskap.norgeNeste12 = engangsstønad.utenlandsopphold.iNorgeNeste12Mnd;
        this.ytelse.medlemsskap.arbeidSiste12 = engangsstønad.utenlandsopphold.jobbetINorgeSiste12Mnd ? "ARBEIDET_I_NORGE" : "ARBEIDET_I_UTLANDET";
        this.ytelse.medlemsskap.fødselNorge = engangsstønad.utenlandsopphold.fødselINorge;

        this.ytelse.relasjonTilBarn.type = engangsstønad.barn.erBarnetFødt ? "fødsel" : "termin";
        this.ytelse.relasjonTilBarn.antallBarn = engangsstønad.barn.antallBarn;
        this.ytelse.relasjonTilBarn.terminDato = engangsstønad.barn.termindato;
        this.ytelse.relasjonTilBarn.utstedtDato = engangsstønad.barn.terminbekreftelseDato;

    }
}
