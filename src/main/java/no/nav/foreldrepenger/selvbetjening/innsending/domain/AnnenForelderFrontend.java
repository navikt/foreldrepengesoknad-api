package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AnnenForelderFrontend(boolean kanIkkeOppgis,
                                    String fornavn,
                                    String etternavn,
                                    String fnr,
                                    boolean utenlandskFnr,
                                    String bostedsland,
                                    boolean harRettPåForeldrepenger,
                                    boolean erInformertOmSøknaden,
                                    boolean harMorUføretrygd) {

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
            ", fornavn='" + '\'' +
            ", etternavn='" + '\'' +
            ", fnr='" + '\'' +
            ", utenlandskFnr=" + utenlandskFnr +
            ", bostedsland='" + bostedsland + '\'' +
            ", harRettPåForeldrepenger=" + harRettPåForeldrepenger +
            ", erInformertOmSøknaden=" + erInformertOmSøknaden +
            ", harMorUforetrygd=" + harMorUføretrygd +
            '}';
    }
}
