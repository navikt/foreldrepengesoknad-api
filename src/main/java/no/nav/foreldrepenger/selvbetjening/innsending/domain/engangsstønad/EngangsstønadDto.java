package no.nav.foreldrepenger.selvbetjening.innsending.domain.engangsstønad;

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

public record EngangsstønadDto(LocalDate mottattdato,
                               @Valid @NotNull BarnDto barn,
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
