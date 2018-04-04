package no.nav.foreldrepenger.selvbetjening.rest.json;

import java.time.LocalDate;
import java.util.List;

public class Barn {

    public Boolean erBarnetFødt;
    public List<LocalDate> fødselsdatoer;
    public Integer antallBarn;
    public LocalDate termindato;
    public LocalDate terminbekreftelseDato;
}
