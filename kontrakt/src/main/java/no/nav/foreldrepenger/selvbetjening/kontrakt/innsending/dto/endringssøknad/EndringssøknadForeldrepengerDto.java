package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad;

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
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.AnnenforelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Situasjon;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering.VedlegglistestørrelseConstraint;

public record EndringssøknadForeldrepengerDto(LocalDate mottattdato,
                                              @NotNull Situasjon situasjon,
                                              @Valid @NotNull Saksnummer saksnummer,
                                              @Valid @NotNull SøkerDto søker,
                                              @Valid @NotNull BarnDto barn,
                                              @Valid @NotNull AnnenforelderDto annenforelder,
                                              @Pattern(regexp = FRITEKST) String tilleggsopplysninger,
                                              Boolean ønskerJustertUttakVedFødsel,
                                              @Valid @Size(max = 100) List<@Valid @NotNull UttaksplanPeriodeDto> uttaksplan,
                                              @Valid @VedlegglistestørrelseConstraint List<@Valid VedleggDto> vedlegg) implements EndringssøknadDto {

    public EndringssøknadForeldrepengerDto {
        uttaksplan = Optional.ofNullable(uttaksplan).orElse(List.of());
        vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }
}
