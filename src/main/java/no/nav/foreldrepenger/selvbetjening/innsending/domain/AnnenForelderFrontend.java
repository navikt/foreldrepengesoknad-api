package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;
import static no.nav.foreldrepenger.common.util.StringUtil.mask;

import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AnnenForelderFrontend(boolean kanIkkeOppgis,
                                    @Pattern(regexp = FRITEKST) String fornavn,
                                    @Pattern(regexp = FRITEKST) String etternavn,
                                    @Pattern(regexp = FRITEKST) String fnr,
                                    boolean utenlandskFnr,
                                    @Pattern(regexp = BARE_BOKSTAVER) String bostedsland,
                                    boolean harRettPåForeldrepenger,
                                    Boolean erInformertOmSøknaden,
                                    Boolean harMorUføretrygd,
                                    Boolean harAnnenForelderOppholdtSegIEØS,
                                    Boolean harAnnenForelderTilsvarendeRettEØS) {

    @JsonIgnore
    public String type() {
        if (kanIkkeOppgis()) {
            return "ukjent";
        }
        if (utenlandskFnr()) {
            return "utenlandsk";
        }
        return "norsk";
    }

    @Override
    public String toString() {
        return "AnnenForelderFrontend{" +
            "kanIkkeOppgis=" + kanIkkeOppgis +
            ", fornavn='" + mask(fornavn) + '\'' +
            ", etternavn='" + mask(etternavn) + '\'' +
            ", fnr='" + mask(fnr) + '\'' +
            ", utenlandskFnr=" + utenlandskFnr +
            ", bostedsland='" + bostedsland + '\'' +
            ", harRettPåForeldrepenger=" + harRettPåForeldrepenger +
            ", erInformertOmSøknaden=" + erInformertOmSøknaden +
            ", harMorUføretrygd=" + harMorUføretrygd +
            ", harAnnenForelderOppholdtSegIEØS=" + harAnnenForelderOppholdtSegIEØS +
            ", harAnnenForelderTilsvarendeRettEØS=" + harAnnenForelderTilsvarendeRettEØS +
            '}';
    }
}
