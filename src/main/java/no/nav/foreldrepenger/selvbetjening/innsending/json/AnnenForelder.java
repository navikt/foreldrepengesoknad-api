package no.nav.foreldrepenger.selvbetjening.innsending.json;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

public class AnnenForelder {
    public Boolean kanIkkeOppgis;
    public String navn;
    public String fnr;
    public Boolean utenlandskFnr;
    public String bostedsland;
    public Boolean harRettPåForeldrepenger;
    public Boolean erInformertOmSøknaden;

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

}
