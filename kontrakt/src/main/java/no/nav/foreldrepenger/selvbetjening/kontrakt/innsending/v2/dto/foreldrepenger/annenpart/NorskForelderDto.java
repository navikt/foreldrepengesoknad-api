package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.annenpart;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;

public record NorskForelderDto(@NotNull Fødselsnummer fnr,
                               @NotNull @Pattern(regexp = FRITEKST) String fornavn,
                               @NotNull @Pattern(regexp = FRITEKST) String etternavn,
                               @NotNull Rettigheter rettigheter) implements AnnenForelderDto {
}
