package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record BarnDtoOLD(@Valid @Size(max = 10) List<@PastOrPresent LocalDate> fødselsdatoer, @Digits(integer = 2, fraction = 0) int antallBarn,
                         LocalDate termindato, LocalDate terminbekreftelseDato, LocalDate adopsjonsdato, LocalDate ankomstdato,
                         boolean adopsjonAvEktefellesBarn, boolean søkerAdopsjonAlene, LocalDate foreldreansvarsdato) {
    public BarnDtoOLD {
        fødselsdatoer = Optional.ofNullable(fødselsdatoer).orElse(emptyList());
    }

}
