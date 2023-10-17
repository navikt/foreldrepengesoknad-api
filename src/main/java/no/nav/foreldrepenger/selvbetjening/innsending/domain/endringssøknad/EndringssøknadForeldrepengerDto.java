package no.nav.foreldrepenger.selvbetjening.innsending.domain.endringssøknad;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.BarnDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.foreldrepenger.AnnenforelderDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.foreldrepenger.Situasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.foreldrepenger.UttaksplanPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.validering.VedlegglistestørrelseConstraint;

public record EndringssøknadForeldrepengerDto(LocalDate mottattdato,
                                              @NotNull Situasjon situasjon,
                                              @Valid @NotNull Saksnummer saksnummer,
                                              @Valid @NotNull SøkerDto søker,
                                              @Valid @NotNull BarnDto barn,
                                              @Valid @NotNull AnnenforelderDto annenforelder,
                                              @Pattern(regexp = FRITEKST) String tilleggsopplysninger,
                                              @Nullable Boolean ønskerJustertUttakVedFødsel,
                                              @Valid @Size(max = 100) List<@Valid @NotNull UttaksplanPeriodeDto> uttaksplan,
                                              @Valid @VedlegglistestørrelseConstraint List<@Valid VedleggDto> vedlegg) implements EndringssøknadDto {

    public EndringssøknadForeldrepengerDto {
        uttaksplan = Optional.ofNullable(uttaksplan).orElse(List.of());
        vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }
}
