package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.AnnenforelderDtoOLD;

public class AnnenforelderBuilder {
    private Boolean kanIkkeOppgis;
    private String fornavn;
    private String etternavn;
    private String fnr;
    private String bostedsland;
    private Boolean utenlandskFnr;
    private boolean harRettPåForeldrepenger;
    private boolean erInformertOmSøknaden;
    private Boolean harMorUføretrygd;
    private Boolean harAnnenForelderOppholdtSegIEØS;
    private Boolean harAnnenForelderTilsvarendeRettEØS;

    public static AnnenforelderDtoOLD ukjentForelder() {
        return new AnnenforelderBuilder().medKanIkkeOppgis(true).build();
    }

    public static AnnenforelderBuilder norskMedRettighetNorge(Fødselsnummer fnr) {
        return new AnnenforelderBuilder().medUtenlandskFnr(false)
            .medHarRettPåForeldrepenger(true)
            .medErInformertOmSøknaden(true)
            .medFnr(fnr.value())
            .medBostedsland(CountryCode.NO.getAlpha2());
    }

    public static AnnenforelderBuilder norskIkkeRett(Fødselsnummer fnr) {
        return new AnnenforelderBuilder().medUtenlandskFnr(false)
            .medHarRettPåForeldrepenger(false)
            .medErInformertOmSøknaden(false)
            .medHarMorUføretrygd(false)
            .medHarAnnenForelderTilsvarendeRettEØS(false)
            .medFnr(fnr.value())
            .medBostedsland(CountryCode.NO.getAlpha2());
    }

    public static AnnenforelderBuilder annenpartIkkeRettOgMorHarUføretrygd(Fødselsnummer fnr) {
        return new AnnenforelderBuilder().medHarRettPåForeldrepenger(false)
            .medErInformertOmSøknaden(true)
            .medHarMorUføretrygd(true)
            .medBostedsland(CountryCode.NO.getAlpha2())
            .medFnr(fnr.value());
    }

    private AnnenforelderBuilder() {
    }

    public AnnenforelderBuilder medKanIkkeOppgis(Boolean kanIkkeOppgis) {
        this.kanIkkeOppgis = kanIkkeOppgis;
        return this;
    }

    public AnnenforelderBuilder medFornavn(String fornavn) {
        this.fornavn = fornavn;
        return this;
    }

    public AnnenforelderBuilder medEtternavn(String etternavn) {
        this.etternavn = etternavn;
        return this;
    }

    public AnnenforelderBuilder medFnr(String fnr) {
        this.fnr = fnr;
        return this;
    }

    public AnnenforelderBuilder medBostedsland(String bostedsland) {
        this.bostedsland = bostedsland;
        return this;
    }

    public AnnenforelderBuilder medUtenlandskFnr(Boolean utenlandskFnr) {
        this.utenlandskFnr = utenlandskFnr;
        return this;
    }

    public AnnenforelderBuilder medHarRettPåForeldrepenger(boolean harRettPåForeldrepenger) {
        this.harRettPåForeldrepenger = harRettPåForeldrepenger;
        return this;
    }

    public AnnenforelderBuilder medErInformertOmSøknaden(boolean erInformertOmSøknaden) {
        this.erInformertOmSøknaden = erInformertOmSøknaden;
        return this;
    }

    public AnnenforelderBuilder medHarMorUføretrygd(Boolean harMorUføretrygd) {
        this.harMorUføretrygd = harMorUføretrygd;
        return this;
    }

    public AnnenforelderBuilder medHarAnnenForelderOppholdtSegIEØS(Boolean harAnnenForelderOppholdtSegIEØS) {
        this.harAnnenForelderOppholdtSegIEØS = harAnnenForelderOppholdtSegIEØS;
        return this;
    }

    public AnnenforelderBuilder medHarAnnenForelderTilsvarendeRettEØS(Boolean harAnnenForelderTilsvarendeRettEØS) {
        this.harAnnenForelderTilsvarendeRettEØS = harAnnenForelderTilsvarendeRettEØS;
        return this;
    }

    public AnnenforelderDtoOLD build() {
        return new AnnenforelderDtoOLD(kanIkkeOppgis, fornavn, etternavn, fnr, bostedsland, utenlandskFnr, harRettPåForeldrepenger,
            erInformertOmSøknaden, harMorUføretrygd, harAnnenForelderOppholdtSegIEØS, harAnnenForelderTilsvarendeRettEØS);
    }
}
