package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

import java.time.LocalDate;

public class AnnenForelder {

    public Boolean kanIkkeOppgis;
    public String fornavn;
    public String etternavn;
    public String fnr;
    public Boolean utenlandskFnr;
    public String bostedsland;
    public Boolean harRettPåForeldrepenger;
    public Boolean erInformertOmSøknaden;
    public Boolean erForSyk;
    public LocalDate datoForAleneomsorg;

    public String type() {
        if (isTrue(kanIkkeOppgis)) {
            return "ukjent";
        }
        else if (isTrue(utenlandskFnr)) {
            return "utenlandsk";
        }
        else {
            return "norsk";
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [kanIkkeOppgis=" + kanIkkeOppgis + ", fornavn=" + fornavn + ", etternavn="
                + etternavn + ", fnr=" + fnr + ", utenlandskFnr=" + utenlandskFnr + ", bostedsland=" + bostedsland
                + ", harRettPåForeldrepenger=" + harRettPåForeldrepenger + ", erInformertOmSøknaden="
                + erInformertOmSøknaden + ", erForSyk=" + erForSyk + ", datoForAleneomsorg=" + datoForAleneomsorg + "]";
    }

}
