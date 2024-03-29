package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.engangsstønad;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering.VedlegglistestørrelseConstraint;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;

public record EngangsstønadDto(LocalDate mottattdato,
                               @NotNull Målform språkkode,
                               @Valid @NotNull BarnDto barn,
                               @Valid @Size(max = 40) @JsonAlias("oppholdIUtlandet") List<@Valid @NotNull UtenlandsoppholdsperiodeDto> utenlandsopphold, // TODO: Fjern etter omlegging
                               @Valid @VedlegglistestørrelseConstraint @Size(max = 100) List<@Valid @NotNull VedleggDto> vedlegg) implements SøknadDto {

    public EngangsstønadDto {
        vedlegg = Optional.ofNullable(vedlegg).orElse(List.of());
    }
}
