package no.nav.foreldrepenger.selvbetjening.consumer.json;

import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstønad;

import java.time.LocalDateTime;

public class EngangsstønadDto {

    public LocalDateTime mottattdato;

    public SøkerDto søker;
    public YtelseDto ytelse;

    public EngangsstønadDto() {}

    public EngangsstønadDto(Engangsstønad engangsstønad, String fnr, String aktørId) {
        this.søker = new SøkerDto();
        this.ytelse = new YtelseDto();

        this.søker.fornavn = "Lille-Mor";
        this.søker.etternavn = "Brisnes";
        this.søker.fnr = fnr;
        this.søker.aktør = aktørId;
        this.søker.søknadsRolle = "MOR";

        this.ytelse.type = "engangsstønad";

        this.mottattdato = LocalDateTime.now();

        this.ytelse.medlemsskap.norgeSiste12 = engangsstønad.utenlandsopphold.iNorgeSiste12Mnd;
        this.ytelse.medlemsskap.norgeNeste12 = engangsstønad.utenlandsopphold.iNorgeNeste12Mnd;
        this.ytelse.medlemsskap.arbeidSiste12 = "IKKE_ARBEIDET";
        this.ytelse.medlemsskap.fødselNorge = engangsstønad.utenlandsopphold.fødselINorge;

        this.ytelse.relasjonTilBarn.type = engangsstønad.barn.erBarnetFødt ? "fødsel" : "termin";
        this.ytelse.relasjonTilBarn.antallBarn = engangsstønad.barn.antallBarn;
        this.ytelse.relasjonTilBarn.terminDato = engangsstønad.barn.termindato;
        this.ytelse.relasjonTilBarn.utstedtDato = engangsstønad.barn.terminbekreftelseDato;
        this.ytelse.relasjonTilBarn.fødselsdato = engangsstønad.barn.fødselsdato();
    }
}
