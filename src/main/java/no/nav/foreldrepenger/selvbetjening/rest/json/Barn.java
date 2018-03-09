package no.nav.foreldrepenger.selvbetjening.rest.json;

import java.time.LocalDate;
import java.util.List;

public class Barn {

    public Boolean erBarnetFødt;
    public List<LocalDate> fødselsdatoer;
    public Integer antallBarn;
    public LocalDate termindato;
    public LocalDate terminbekreftelseDato;

    public LocalDate fødselsdato() {
        if (fødselsdatoer != null && fødselsdatoer.size() > 0) {
            return fødselsdatoer.get(0);
        } else {
            return null;
        }
    }

}
