package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SøknadsGrunnlag {
    private final FamiliehendelseType familieHendelseType;
    private final LocalDate familieHendelseDato;
    private final Integer dekningsgrad;
    private final Integer antallBarn;
    private final Boolean søkerErFarEllerMedmor;
    private final Boolean morErAleneOmOmsorg;
    private final Boolean morHarRett;
    private final Boolean morErUfør;
    private final Boolean farMedmorErAleneOmOmsorg;
    private final Boolean farMedmorHarRett;
    private final Boolean annenForelderErInformert;

    @JsonCreator
    public SøknadsGrunnlag(@JsonProperty("familieHendelseType") FamiliehendelseType familieHendelseType,
            @JsonProperty("familieHendelseDato") LocalDate familieHendelseDato,
            @JsonProperty("dekningsgrad") Integer dekningsgrad,
            @JsonProperty("antallBarn") Integer antallBarn,
            @JsonProperty("søkerErFarEllerMedmor") Boolean søkerErFarEllerMedmor,
            @JsonProperty("morErAleneOmOmsorg") Boolean morErAleneOmOmsorg,
            @JsonProperty("morHarRett") Boolean morHarRett,
            @JsonProperty("morErUfør") Boolean morErUfør,
            @JsonProperty("farMedmorErAleneOmOmsorg") Boolean farMedmorErAleneOmOmsorg,
            @JsonProperty("farMedmorHarRett") Boolean farMedmorHarRett,
            @JsonProperty("annenForelderErInformert") Boolean annenForelderErInformert) {
        this.familieHendelseType = familieHendelseType;
        this.familieHendelseDato = familieHendelseDato;
        this.dekningsgrad = dekningsgrad;
        this.antallBarn = antallBarn;
        this.søkerErFarEllerMedmor = søkerErFarEllerMedmor;
        this.morErAleneOmOmsorg = morErAleneOmOmsorg;
        this.morHarRett = morHarRett;
        this.morErUfør = morErUfør;
        this.farMedmorErAleneOmOmsorg = farMedmorErAleneOmOmsorg;
        this.farMedmorHarRett = farMedmorHarRett;
        this.annenForelderErInformert = annenForelderErInformert;
    }

    public Boolean getAnnenForelderErInformert() {
        return annenForelderErInformert;
    }

    public FamiliehendelseType getFamilieHendelseType() {
        return familieHendelseType;
    }

    public LocalDate getFamilieHendelseDato() {
        return familieHendelseDato;
    }

    public Integer getDekningsgrad() {
        return dekningsgrad;
    }

    public Integer getAntallBarn() {
        return antallBarn;
    }

    public Boolean getSøkerErFarEllerMedmor() {
        return søkerErFarEllerMedmor;
    }

    public Boolean getMorErAleneOmOmsorg() {
        return morErAleneOmOmsorg;
    }

    public Boolean getMorHarRett() {
        return morHarRett;
    }

    public Boolean getMorErUfør() {
        return morErUfør;
    }

    public Boolean getFarMedmorErAleneOmOmsorg() {
        return farMedmorErAleneOmOmsorg;
    }

    public Boolean getFarMedmorHarRett() {
        return farMedmorHarRett;
    }

    @Override
    public int hashCode() {
        return Objects.hash(farMedmorHarRett, farMedmorErAleneOmOmsorg, morErUfør, morHarRett, morErAleneOmOmsorg,
                søkerErFarEllerMedmor, antallBarn, dekningsgrad, familieHendelseDato, familieHendelseType,
                annenForelderErInformert);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SøknadsGrunnlag that = (SøknadsGrunnlag) o;
        return Objects.equals(farMedmorHarRett, that.farMedmorHarRett)
                && Objects.equals(this.farMedmorErAleneOmOmsorg, that.farMedmorErAleneOmOmsorg)
                && Objects.equals(this.morErUfør, that.morErUfør)
                && Objects.equals(this.morHarRett, that.morHarRett)
                && Objects.equals(this.morErAleneOmOmsorg, that.morErAleneOmOmsorg)
                && Objects.equals(this.søkerErFarEllerMedmor, that.søkerErFarEllerMedmor)
                && Objects.equals(this.antallBarn, that.antallBarn)
                && Objects.equals(this.dekningsgrad, that.dekningsgrad)
                && Objects.equals(this.familieHendelseDato, that.familieHendelseDato)
                && Objects.equals(this.annenForelderErInformert, that.annenForelderErInformert)
                && Objects.equals(this.familieHendelseType, that.familieHendelseType);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [familieHendelseType=" + familieHendelseType + ", familieHendelseDato="
                + familieHendelseDato + ", dekningsgrad=" + dekningsgrad + ", antallBarn=" + antallBarn
                + ", søkerErFarEllerMedmor=" + søkerErFarEllerMedmor + ", morErAleneOmOmsorg=" + morErAleneOmOmsorg
                + ", morHarRett=" + morHarRett + ", morErUfør=" + morErUfør + ", farMedmorErAleneOmOmsorg="
                + farMedmorErAleneOmOmsorg + ", farMedmorHarRett=" + farMedmorHarRett + ", annenForelderErInformert="
                + annenForelderErInformert + "]";
    }
}
