package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;

@JsonInclude(NON_EMPTY)
public record BarnFrontend(@Valid @Size(max = 10) List<LocalDate> fødselsdatoer,
                           @Digits(integer = 2, fraction = 0) int antallBarn,
                           @Valid @Size(max = 15) List<VedleggReferanse> terminbekreftelse,
                           LocalDate termindato,
                           LocalDate terminbekreftelseDato,
                           LocalDate adopsjonsdato,
                           @Valid @Size(max = 15) List<VedleggReferanse> adopsjonsvedtak,
                           LocalDate ankomstdato,
                           boolean adopsjonAvEktefellesBarn,
                           boolean søkerAdopsjonAlene,
                           LocalDate foreldreansvarsdato,
                           @Valid @Size(max = 15) List<VedleggReferanse> omsorgsovertakelse,
                           @Valid @Size(max = 15) List<VedleggReferanse> dokumentasjonAvAleneomsorg) {

    public BarnFrontend(List<LocalDate> fødselsdatoer, int antallBarn, List<VedleggReferanse> terminbekreftelse, LocalDate termindato,
                        LocalDate terminbekreftelseDato, LocalDate adopsjonsdato, List<VedleggReferanse> adopsjonsvedtak,
                        LocalDate ankomstdato, boolean adopsjonAvEktefellesBarn, boolean søkerAdopsjonAlene,
                        LocalDate foreldreansvarsdato, List<VedleggReferanse> omsorgsovertakelse, List<VedleggReferanse> dokumentasjonAvAleneomsorg) {
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
    public List<VedleggReferanse> getAlleVedlegg() {
        List<VedleggReferanse> alleVedlegg = new ArrayList<>();
        alleVedlegg.addAll(terminbekreftelse);
        alleVedlegg.addAll(omsorgsovertakelse);
        alleVedlegg.addAll(adopsjonsvedtak);
        alleVedlegg.addAll(dokumentasjonAvAleneomsorg);
        return alleVedlegg;
    }
}
