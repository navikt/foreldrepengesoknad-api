package no.nav.foreldrepenger.selvbetjening.innsending.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class Barn {

    public Boolean erBarnetFødt;
    public List<LocalDate> fødselsdatoer;
    public Integer antallBarn;
    public List<String> terminbekreftelse;
    public LocalDate termindato;
    public LocalDate terminbekreftelseDato;

    public LocalDate adopsjonsdato;
    public Boolean adoptertIUtlandet;

    public LocalDate foreldreansvarsdato;

}
