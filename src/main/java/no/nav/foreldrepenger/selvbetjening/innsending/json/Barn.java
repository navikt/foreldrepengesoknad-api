package no.nav.foreldrepenger.selvbetjening.innsending.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class Barn {

    public Boolean erBarnetFødt;
    public List<LocalDate> fødselsdatoer;
    public Integer antallBarn;
    public List<String> terminbekreftelse = new ArrayList<>();
    public LocalDate termindato;
    public LocalDate terminbekreftelseDato;


    public LocalDate adopsjonsdato;
    public List<String> adopsjonsvedtak = new ArrayList<>();
    public LocalDate ankomstdato;
    public Boolean adopsjonAvEktefellesBarn;

    public LocalDate foreldreansvarsdato;
    public List<String> omsorgsovertakelse = new ArrayList<>();

    public List<String> dokumentasjonAvAleneomsorg = new ArrayList<>();

    public List<String> getAlleVedlegg() {
        List<String> alleVedlegg = new ArrayList<>();
        alleVedlegg.addAll(terminbekreftelse);
        alleVedlegg.addAll(omsorgsovertakelse);
        alleVedlegg.addAll(adopsjonsvedtak);
        alleVedlegg.addAll(dokumentasjonAvAleneomsorg);
        return alleVedlegg;
    }

}
