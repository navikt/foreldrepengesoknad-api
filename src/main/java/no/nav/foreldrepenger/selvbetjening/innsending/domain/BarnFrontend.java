package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public record BarnFrontend(List<LocalDate> fødselsdatoer,
                           int antallBarn,
                           List<@Pattern(regexp = FRITEKST) String> terminbekreftelse,
                           LocalDate termindato,
                           LocalDate terminbekreftelseDato,
                           LocalDate adopsjonsdato,
                           List<@Pattern(regexp = FRITEKST) String> adopsjonsvedtak,
                           LocalDate ankomstdato,
                           boolean adopsjonAvEktefellesBarn,
                           boolean søkerAdopsjonAlene,
                           LocalDate foreldreansvarsdato,
                           List<@Pattern(regexp = FRITEKST) String> omsorgsovertakelse,
                           List<@Pattern(regexp = FRITEKST) String> dokumentasjonAvAleneomsorg) {

    public BarnFrontend(List<LocalDate> fødselsdatoer, int antallBarn, List<String> terminbekreftelse, LocalDate termindato,
                        LocalDate terminbekreftelseDato, LocalDate adopsjonsdato, List<String> adopsjonsvedtak,
                        LocalDate ankomstdato, boolean adopsjonAvEktefellesBarn, boolean søkerAdopsjonAlene,
                        LocalDate foreldreansvarsdato, List<String> omsorgsovertakelse, List<String> dokumentasjonAvAleneomsorg) {
        this.fødselsdatoer = fødselsdatoer;
        this.antallBarn = antallBarn;
        this.terminbekreftelse = Optional.ofNullable(terminbekreftelse).orElse(emptyList());
        this.termindato = termindato;
        this.terminbekreftelseDato = terminbekreftelseDato;
        this.adopsjonsdato = adopsjonsdato;
        this.adopsjonsvedtak = Optional.ofNullable(adopsjonsvedtak).orElse(emptyList());
        this.ankomstdato = ankomstdato;
        this.adopsjonAvEktefellesBarn = adopsjonAvEktefellesBarn;
        this.søkerAdopsjonAlene = søkerAdopsjonAlene;
        this.foreldreansvarsdato = foreldreansvarsdato;
        this.omsorgsovertakelse = Optional.ofNullable(omsorgsovertakelse).orElse(emptyList());
        this.dokumentasjonAvAleneomsorg = Optional.ofNullable(dokumentasjonAvAleneomsorg).orElse(emptyList());
    }

    @JsonIgnore
    public List<String> getAlleVedlegg() {
        List<String> alleVedlegg = new ArrayList<>();
        alleVedlegg.addAll(terminbekreftelse);
        alleVedlegg.addAll(omsorgsovertakelse);
        alleVedlegg.addAll(adopsjonsvedtak);
        alleVedlegg.addAll(dokumentasjonAvAleneomsorg);
        return alleVedlegg;
    }
}
