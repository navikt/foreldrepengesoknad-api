package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Situasjon;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering.VedlegglistestørrelseConstraint;

public record EngangsstønadDto(LocalDate mottattdato,
                               @Valid @NotNull BarnDto barn,
                               @Valid @NotNull UtenlandsoppholdDto informasjonOmUtenlandsopphold,
                               @Valid @NotNull SøkerDto søker,
                               @Valid @VedlegglistestørrelseConstraint @Size(max = 100) List<@Valid VedleggDto> vedlegg) implements SøknadDto {

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
