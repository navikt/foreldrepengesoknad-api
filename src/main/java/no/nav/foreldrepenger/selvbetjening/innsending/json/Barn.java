package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Barn {

    public Boolean erBarnetFødt;
    public List<LocalDate> fødselsdatoer;
    public Integer antallBarn;
    public List<String> terminbekreftelse = new ArrayList<>();
    public LocalDate termindato;
    public LocalDate terminbekreftelseDato;


    public LocalDate adopsjonsdato;
    public List<String> adopsjonsvedtak = new ArrayList<>();
    public Boolean adoptertIUtlandet;

    public LocalDate foreldreansvarsdato;
    public List<String> omsorgsovertakelse = new ArrayList<>();

    public List<String> getAlleVedlegg() {
        List<String> alleVedlegg = new ArrayList<>();
        alleVedlegg.addAll(terminbekreftelse);
        alleVedlegg.addAll(omsorgsovertakelse);
        alleVedlegg.addAll(adopsjonsvedtak);
        return alleVedlegg;
    }

}
