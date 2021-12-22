package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AnnenForelder(boolean kanIkkeOppgis,
                            String fornavn,
                            String etternavn,
                            String navn,
                            String fnr,
                            boolean utenlandskFnr,
                            String bostedsland,
                            boolean harRettPåForeldrepenger,
                            boolean erInformertOmSøknaden,
                            boolean erForSyk,
                            LocalDate datoForAleneomsorg) {

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
        return "AnnenForelder{" +
            "kanIkkeOppgis=" + kanIkkeOppgis +
            ", fornavn='" + fornavn + '\'' +
            ", etternavn='IKKE_LOGG'" +
            ", navn='IKKE_LOGG'" + navn + '\'' +
            ", fnr=IKKE_LOGG'" +
            ", utenlandskFnr=IKKE_LOGG" +
            ", bostedsland='" + bostedsland + '\'' +
            ", harRettPåForeldrepenger=" + harRettPåForeldrepenger +
            ", erInformertOmSøknaden=" + erInformertOmSøknaden +
            ", erForSyk=" + erForSyk +
            ", datoForAleneomsorg=" + datoForAleneomsorg +
            '}';
    }
}
