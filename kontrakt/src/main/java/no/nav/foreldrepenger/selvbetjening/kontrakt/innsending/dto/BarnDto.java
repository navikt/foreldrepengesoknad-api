package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

public record BarnDto(@Valid @Size(max = 10) List<LocalDate> fødselsdatoer,
                      @Digits(integer = 2, fraction = 0) int antallBarn,
                      LocalDate termindato,
                      LocalDate terminbekreftelseDato,
                      LocalDate adopsjonsdato,
                      LocalDate ankomstdato,
                      boolean adopsjonAvEktefellesBarn,
                      boolean søkerAdopsjonAlene,
                      LocalDate foreldreansvarsdato,
                      @Valid @Size(max = 15) List<MutableVedleggReferanseDto> terminbekreftelse,
                      @Valid @Size(max = 15) List<MutableVedleggReferanseDto> adopsjonsvedtak,
                      @Valid @Size(max = 15) List<MutableVedleggReferanseDto> omsorgsovertakelse,
                      @Valid @Size(max = 15) List<MutableVedleggReferanseDto> dokumentasjonAvAleneomsorg) {
    public BarnDto {
        fødselsdatoer = Optional.ofNullable(fødselsdatoer).orElse(emptyList());
        terminbekreftelse = Optional.ofNullable(terminbekreftelse).orElse(emptyList());
        adopsjonsvedtak = Optional.ofNullable(adopsjonsvedtak).orElse(emptyList());
        omsorgsovertakelse = Optional.ofNullable(omsorgsovertakelse).orElse(emptyList());
        dokumentasjonAvAleneomsorg = Optional.ofNullable(dokumentasjonAvAleneomsorg).orElse(emptyList());
    }
    @JsonIgnore
    public List<MutableVedleggReferanseDto> getAlleVedlegg() {
        List<MutableVedleggReferanseDto> alleVedlegg = new ArrayList<>();
        alleVedlegg.addAll(terminbekreftelse);
        alleVedlegg.addAll(omsorgsovertakelse);
        alleVedlegg.addAll(adopsjonsvedtak);
        alleVedlegg.addAll(dokumentasjonAvAleneomsorg);
        return alleVedlegg;
    }
}
