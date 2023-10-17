package no.nav.foreldrepenger.selvbetjening.innsending.domain.foreldrepenger;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.BarnDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.validering.VedlegglistestørrelseConstraint;

public record ForeldrepengesøknadDto(LocalDate mottattdato,
                                     @NotNull Situasjon situasjon,
                                     @Valid @NotNull SøkerDto søker,
                                     @Valid @NotNull BarnDto barn,
                                     @Valid @NotNull AnnenforelderDto annenForelder,
                                     @Valid @NotNull Dekningsgrad dekningsgrad,
                                     @Pattern(regexp = FRITEKST) String tilleggsopplysninger,
                                     @Valid @NotNull UtenlandsoppholdDto informasjonOmUtenlandsopphold,
                                     @Valid List<@Valid @NotNull UttaksplanPeriodeDto> uttaksplan,
                                     @Nullable Boolean ønskerJustertUttakVedFødsel,
                                     @Valid @VedlegglistestørrelseConstraint List<@Valid VedleggDto> vedlegg) implements SøknadDto {
    public ForeldrepengesøknadDto {
        uttaksplan = Optional.ofNullable(uttaksplan).orElse(List.of());
        vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }
}
