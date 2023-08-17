package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Collections.emptyList;

@JsonInclude(NON_EMPTY)
public record BarnFrontend(@Valid @Size(max = 10) List<LocalDate> fødselsdatoer,
                           @Digits(integer = 2, fraction = 0) int antallBarn,
                           @Valid @Size(max = 15) List<MutableVedleggReferanse> terminbekreftelse,
                           LocalDate termindato,
                           LocalDate terminbekreftelseDato,
                           LocalDate adopsjonsdato,
                           @Valid @Size(max = 15) List<MutableVedleggReferanse> adopsjonsvedtak,
                           LocalDate ankomstdato,
                           boolean adopsjonAvEktefellesBarn,
                           boolean søkerAdopsjonAlene,
                           LocalDate foreldreansvarsdato,
                           @Valid @Size(max = 15) List<MutableVedleggReferanse> omsorgsovertakelse,
                           @Valid @Size(max = 15) List<MutableVedleggReferanse> dokumentasjonAvAleneomsorg) {

    public BarnFrontend(List<LocalDate> fødselsdatoer, int antallBarn, List<MutableVedleggReferanse> terminbekreftelse, LocalDate termindato,
                        LocalDate terminbekreftelseDato, LocalDate adopsjonsdato, List<MutableVedleggReferanse> adopsjonsvedtak,
                        LocalDate ankomstdato, boolean adopsjonAvEktefellesBarn, boolean søkerAdopsjonAlene,
                        LocalDate foreldreansvarsdato, List<MutableVedleggReferanse> omsorgsovertakelse, List<MutableVedleggReferanse> dokumentasjonAvAleneomsorg) {
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
    public List<MutableVedleggReferanse> getAlleVedlegg() {
        List<MutableVedleggReferanse> alleVedlegg = new ArrayList<>();
        alleVedlegg.addAll(terminbekreftelse);
        alleVedlegg.addAll(omsorgsovertakelse);
        alleVedlegg.addAll(adopsjonsvedtak);
        alleVedlegg.addAll(dokumentasjonAvAleneomsorg);
        return alleVedlegg;
    }
}
