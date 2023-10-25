package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

public record OmsorgsovertakelseDto(@Min(1) int antallBarn,
                                    @Size(min = 1, max = 10) List<LocalDate> fødselsdatoer,
                                    @NotNull LocalDate foreldreansvarsdato,
                                    @Size(max = 15) List<@Valid MutableVedleggReferanseDto> vedlegg) implements BarnDto {

    public OmsorgsovertakelseDto {
        vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
    }
}
