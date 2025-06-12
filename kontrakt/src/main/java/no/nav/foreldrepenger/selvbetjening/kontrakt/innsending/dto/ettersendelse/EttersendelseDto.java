package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

public record EttersendelseDto(LocalDate mottattdato, @NotNull YtelseType type, @NotNull @Valid Saksnummer saksnummer,
                               @Valid BrukerTekstDto brukerTekst, @Pattern(regexp = FRITEKST) String dialogId,
                               @Valid @NotNull @Size(max = 40) List<@Valid VedleggDto> vedlegg) implements Innsending {

    public EttersendelseDto {
        vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }

    public boolean erTilbakebetalingUttalelse() {
        return dialogId != null;
    }

    @Override
    public String navn() {
        return "ettersendelse";
    }
}
