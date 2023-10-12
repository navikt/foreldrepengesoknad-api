package no.nav.foreldrepenger.selvbetjening.innsending.dto.engangsstønad;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.validering.VedlegglistestørrelseConstraint;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger.Situasjon;

public record EngangsstønadDto(@Valid @NotNull BarnDto barn,
                               @Valid @NotNull UtenlandsoppholdDto informasjonOmUtenlandsopphold,
                               @Valid @NotNull SøkerDto søker,
                               @Valid @VedlegglistestørrelseConstraint List<@Valid VedleggDto> vedlegg) implements SøknadDto {

    public EngangsstønadDto {
       vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }

    @Override
    public Situasjon situasjon() {
        if (barn.adopsjonsdato() != null) {
            return Situasjon.ADOPSJON;
        } else {
            return Situasjon.FØDSEL;
        }
    }
}
