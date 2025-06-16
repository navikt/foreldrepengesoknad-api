package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.annenpart.AnnenForelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.annenpart.NorskForelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.annenpart.UtenlandskForelderDto;

public class AnnenforelderBuilder {
    public static AnnenForelderDto ukjentForelder() {
        return null;
    }

    public static NorskForelderBuilder norskMedRettighetNorge(Fødselsnummer fnr) {
        return new NorskForelderBuilder(fnr).medHarRettPåForeldrepenger(true).medErInformertOmSøknaden(true);
    }

    public static NorskForelderBuilder norskIkkeRett(Fødselsnummer fnr) {
        return new NorskForelderBuilder(fnr).medErAleneOmOmsorg(false)
            .medHarRettPåForeldrepenger(false)
            .medErInformertOmSøknaden(false)
            .medHarMorUføretrygd(false)
            .medHarAnnenForelderTilsvarendeRettEØS(false);
    }

    public static NorskForelderBuilder aleneomsorgAnnenpartIkkeRettOgMorHarUføretrygd(Fødselsnummer fnr) {
        return new NorskForelderBuilder(fnr).medErAleneOmOmsorg(true)
            .medHarRettPåForeldrepenger(false)
            .medErInformertOmSøknaden(true)
            .medHarMorUføretrygd(true);
    }

    public static UtenlandskForelderBuilder utenlandskForelderRettEØS(Fødselsnummer fnr, CountryCode bostedsland) {
        return new UtenlandskForelderBuilder(fnr, bostedsland).medHarRettPåForeldrepenger(false)
            .medHarAnnenForelderTilsvarendeRettEØS(true)
            .medErInformertOmSøknaden(true)
            .medHarMorUføretrygd(true);
    }


    public static class NorskForelderBuilder {
        private final Fødselsnummer fnr;
        private String fornavn;
        private String etternavn;
        private RettighetBuilder rettigheter = new RettighetBuilder();

        public NorskForelderBuilder(Fødselsnummer fnr) {
            this.fornavn = "Fornavn annenforelder";
            this.etternavn = "Etternavn annenforelder";
            this.fnr = fnr;
        }

        public NorskForelderBuilder medFornavn(String fornavn) {
            this.fornavn = fornavn;
            return this;
        }

        public NorskForelderBuilder medEtternavn(String etternavn) {
            this.etternavn = etternavn;
            return this;
        }


        public NorskForelderBuilder medHarRettPåForeldrepenger(Boolean harRettPåForeldrepenger) {
            this.rettigheter.medHarRettPåForeldrepenger(harRettPåForeldrepenger);
            return this;
        }

        public NorskForelderBuilder medErInformertOmSøknaden(Boolean erInformertOmSøknaden) {
            this.rettigheter.medErInformertOmSøknaden(erInformertOmSøknaden);
            return this;
        }

        public NorskForelderBuilder medErAleneOmOmsorg(Boolean erAleneomsorg) {
            this.rettigheter.medErAleneOmOmsorg(erAleneomsorg);
            return this;
        }

        public NorskForelderBuilder medHarMorUføretrygd(Boolean harMorUføretrygd) {
            this.rettigheter.medHarMorUføretrygd(harMorUføretrygd);
            return this;
        }

        public NorskForelderBuilder medHarAnnenForelderOppholdtSegIEØS(Boolean harAnnenForelderOppholdtSegIEØS) {
            this.rettigheter.medHarAnnenForelderOppholdtSegIEØS(harAnnenForelderOppholdtSegIEØS);
            return this;
        }

        public NorskForelderBuilder medHarAnnenForelderTilsvarendeRettEØS(Boolean harAnnenForelderTilsvarendeRettEØS) {
            this.rettigheter.medHarAnnenForelderTilsvarendeRettEØS(harAnnenForelderTilsvarendeRettEØS);
            return this;
        }

        public NorskForelderDto build() {
            return new NorskForelderDto(fnr, fornavn, etternavn, rettigheter.build());
        }
    }


    public static class UtenlandskForelderBuilder {
        private final Fødselsnummer fnr;
        private final CountryCode bostedsland;
        private String fornavn;
        private String etternavn;
        private RettighetBuilder rettigheter = new RettighetBuilder();

        public UtenlandskForelderBuilder(Fødselsnummer fnr, CountryCode bostedsland) {
            this.fornavn = "Fornavn annenforelder";
            this.etternavn = "Etternavn annenforelder";
            this.fnr = fnr;
            this.bostedsland = bostedsland;
        }

        public UtenlandskForelderBuilder medFornavn(String fornavn) {
            this.fornavn = fornavn;
            return this;
        }

        public UtenlandskForelderBuilder medEtternavn(String etternavn) {
            this.etternavn = etternavn;
            return this;
        }

        public UtenlandskForelderBuilder medHarRettPåForeldrepenger(Boolean harRettPåForeldrepenger) {
            this.rettigheter.medHarRettPåForeldrepenger(harRettPåForeldrepenger);
            return this;
        }

        public UtenlandskForelderBuilder medErInformertOmSøknaden(Boolean erInformertOmSøknaden) {
            this.rettigheter.medErInformertOmSøknaden(erInformertOmSøknaden);
            return this;
        }

        public UtenlandskForelderBuilder medHarMorUføretrygd(Boolean harMorUføretrygd) {
            this.rettigheter.medHarMorUføretrygd(harMorUføretrygd);
            return this;
        }

        public UtenlandskForelderBuilder medErAleneOmOmsorg(Boolean erAleneomsorg) {
            this.rettigheter.medErAleneOmOmsorg(erAleneomsorg);
            return this;
        }

        public UtenlandskForelderBuilder medHarAnnenForelderOppholdtSegIEØS(Boolean harAnnenForelderOppholdtSegIEØS) {
            this.rettigheter.medHarAnnenForelderOppholdtSegIEØS(harAnnenForelderOppholdtSegIEØS);
            return this;
        }

        public UtenlandskForelderBuilder medHarAnnenForelderTilsvarendeRettEØS(Boolean harAnnenForelderTilsvarendeRettEØS) {
            this.rettigheter.medHarAnnenForelderTilsvarendeRettEØS(harAnnenForelderTilsvarendeRettEØS);
            return this;
        }

        public UtenlandskForelderDto build() {
            return new UtenlandskForelderDto(fnr, fornavn, etternavn, bostedsland, rettigheter.build());
        }
    }


    public static class RettighetBuilder {
        private Boolean harRettPåForeldrepenger;
        private Boolean erInformertOmSøknaden;
        private Boolean erAleneOmOmsorg;
        private Boolean harMorUføretrygd;
        private Boolean harAnnenForelderOppholdtSegIEØS;
        private Boolean harAnnenForelderTilsvarendeRettEØS;

        public RettighetBuilder() {
        }

        public RettighetBuilder medHarRettPåForeldrepenger(Boolean harRettPåForeldrepenger) {
            this.harRettPåForeldrepenger = harRettPåForeldrepenger;
            return this;
        }

        public RettighetBuilder medErAleneOmOmsorg(Boolean erAleneOmOmsorg) {
            this.erAleneOmOmsorg = erAleneOmOmsorg;
            return this;
        }

        public RettighetBuilder medErInformertOmSøknaden(Boolean erInformertOmSøknaden) {
            this.erInformertOmSøknaden = erInformertOmSøknaden;
            return this;
        }


        public RettighetBuilder medHarMorUføretrygd(Boolean harMorUføretrygd) {
            this.harMorUføretrygd = harMorUføretrygd;
            return this;
        }

        public RettighetBuilder medHarAnnenForelderOppholdtSegIEØS(Boolean harAnnenForelderOppholdtSegIEØS) {
            this.harAnnenForelderOppholdtSegIEØS = harAnnenForelderOppholdtSegIEØS;
            return this;
        }

        public RettighetBuilder medHarAnnenForelderTilsvarendeRettEØS(Boolean harAnnenForelderTilsvarendeRettEØS) {
            this.harAnnenForelderTilsvarendeRettEØS = harAnnenForelderTilsvarendeRettEØS;
            return this;
        }

        public AnnenForelderDto.Rettigheter build() {
            return new AnnenForelderDto.Rettigheter(harRettPåForeldrepenger,
                erInformertOmSøknaden,
                erAleneOmOmsorg,
                harMorUføretrygd,
                harAnnenForelderOppholdtSegIEØS,
                harAnnenForelderTilsvarendeRettEØS);
        }
    }
}
