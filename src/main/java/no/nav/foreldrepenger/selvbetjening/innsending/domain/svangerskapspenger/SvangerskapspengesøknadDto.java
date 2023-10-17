package no.nav.foreldrepenger.selvbetjening.innsending.domain.svangerskapspenger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.validering.VedlegglistestørrelseConstraint;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.BarnDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.foreldrepenger.Situasjon;

public record SvangerskapspengesøknadDto(LocalDate mottattdato,
                                         @Valid @NotNull List<TilretteleggingDto> tilrettelegging,
                                         @Valid @NotNull BarnDto barn,
                                         @Valid @NotNull UtenlandsoppholdDto informasjonOmUtenlandsopphold,
                                         @Valid @NotNull SøkerDto søker,
                                         @Valid @VedlegglistestørrelseConstraint List<@Valid VedleggDto> vedlegg) implements SøknadDto {

    public SvangerskapspengesøknadDto {
        tilrettelegging = Optional.ofNullable(tilrettelegging).orElse(List.of());
        vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }

    @Override
    public Situasjon situasjon() {
        return Situasjon.FØDSEL;
    }
}
