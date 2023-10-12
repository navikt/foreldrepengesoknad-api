package no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.validering.VedlegglistestørrelseConstraint;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.VedleggDto;

public record ForeldrepengesøknadDto(Situasjon situasjon,
                                     @Valid Saksnummer saksnummer,
                                     @Valid @NotNull AnnenforelderDto annenForelder,
                                     @Valid @NotNull Dekningsgrad dekningsgrad,
                                     @Valid List<@Valid @NotNull UttaksplanPeriodeDto> uttaksplan,
                                     @Nullable Boolean ønskerJustertUttakVedFødsel,
                                     @Pattern(regexp = FRITEKST) String tilleggsopplysninger,
                                     @Valid @NotNull BarnDto barn,
                                     @Valid @NotNull UtenlandsoppholdDto informasjonOmUtenlandsopphold,
                                     @Valid @NotNull SøkerDto søker,
                                     @Valid @VedlegglistestørrelseConstraint List<@Valid VedleggDto> vedlegg) implements SøknadDto {
    public ForeldrepengesøknadDto {
        uttaksplan = Optional.ofNullable(uttaksplan).orElse(List.of());
        vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }
}
