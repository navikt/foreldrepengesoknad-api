package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.common.domain.felles.DokumentType;

public record BrukerTekstDto(@NotNull DokumentType dokumentType,
                             @Pattern(regexp = FRITEKST) String tekst,
                             @Pattern(regexp = FRITEKST) String overskrift) {
}
