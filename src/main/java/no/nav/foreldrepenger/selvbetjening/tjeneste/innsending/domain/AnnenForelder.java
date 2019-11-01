package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnnenForelder {

    private final Boolean kanIkkeOppgis;
    private final String fornavn;
    private final String etternavn;
    private final String navn;
    private final String fnr;
    private final Boolean utenlandskFnr;
    private final String bostedsland;
    private final Boolean harRettPåForeldrepenger;
    private final Boolean erInformertOmSøknaden;
    private final Boolean erForSyk;
    private final LocalDate datoForAleneomsorg;

    @JsonCreator
    public AnnenForelder(@JsonProperty("kanIkkeOppgis") Boolean kanIkkeOppgis,
            @JsonProperty("fornavn") String fornavn,
            @JsonProperty("etternavn") String etternavn,
            @JsonProperty("navn") String navn,
            @JsonProperty("fnr") String fnr,
            @JsonProperty("utenlandskFnr") Boolean utenlandskFnr,
            @JsonProperty("bostedsland") String bostedsland,
            @JsonProperty("harRettPåForeldrepenger") Boolean harRettPåForeldrepenger,
            @JsonProperty("erInformertOmSøknaden") Boolean erInformertOmSøknaden,
            @JsonProperty("erForSyk") Boolean erForSyk,
            @JsonProperty("datoForAleneomsorg") LocalDate datoForAleneomsorg) {
        this.kanIkkeOppgis = kanIkkeOppgis;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.navn = navn;
        this.fnr = fnr;
        this.utenlandskFnr = utenlandskFnr;
        this.bostedsland = bostedsland;
        this.harRettPåForeldrepenger = harRettPåForeldrepenger;
        this.erInformertOmSøknaden = erInformertOmSøknaden;
        this.erForSyk = erForSyk;
        this.datoForAleneomsorg = datoForAleneomsorg;
    }

    public String type() {
        if (isTrue(getKanIkkeOppgis())) {
            return "ukjent";
        }
        if (isTrue(utenlandskFnr)) {
            return "utenlandsk";
        }
        return "norsk";
    }

    public String getFnr() {
        return fnr;
    }

    public Boolean getUtenlandskFnr() {
        return utenlandskFnr;
    }

    public String getBostedsland() {
        return bostedsland;
    }

    public Boolean getHarRettPåForeldrepenger() {
        return harRettPåForeldrepenger;
    }

    public Boolean getErInformertOmSøknaden() {
        return erInformertOmSøknaden;
    }

    public Boolean getErForSyk() {
        return erForSyk;
    }

    public LocalDate getDatoForAleneomsorg() {
        return datoForAleneomsorg;
    }

    public Boolean getKanIkkeOppgis() {
        return kanIkkeOppgis;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String getNavn() {
        return navn;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [kanIkkeOppgis=" + getKanIkkeOppgis() + ", fornavn=" + getFornavn()
                + ", etternavn="
                + getEtternavn() + ", fnr=" + fnr + ", utenlandskFnr=" + utenlandskFnr + ", bostedsland=" + bostedsland
                + ", harRettPåForeldrepenger=" + harRettPåForeldrepenger + ", erInformertOmSøknaden="
                + erInformertOmSøknaden + ", erForSyk=" + erForSyk + ", datoForAleneomsorg=" + datoForAleneomsorg + "]";
    }
}
