package no.nav.foreldrepenger.selvbetjening.innsending.domain.ettersendelse;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggDto;

public record EttersendelseDto(@NotNull YtelseType type,
                               @Valid Saksnummer saksnummer,
                               @Valid BrukerTekstDto brukerTekst,
                               @Pattern(regexp = FRITEKST) String dialogId,
                               @Valid @Size(max = 40) List<VedleggDto> vedlegg) {

    public EttersendelseDto {
        vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }

    public boolean erTilbakebetalingUttalelse() {
        return dialogId != null;
    }
}
