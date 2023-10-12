package no.nav.foreldrepenger.selvbetjening.innsending.dto.endringssøknad;

import java.util.List;

import javax.annotation.Nullable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger.AnnenforelderDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger.UttaksplanPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.validering.VedlegglistestørrelseConstraint;

public record EndringssøknadForeldrepengerDto(@Valid @NotNull Saksnummer saksnummer,
                                              @Valid @NotNull SøkerDto søker,
                                              @Valid @NotNull AnnenforelderDto annenforelder,
                                              @Nullable Boolean ønskerJustertUttakVedFødsel,
                                              @Valid @Size(max = 100) List<@Valid @NotNull UttaksplanPeriodeDto> uttaksplan,
                                              @Valid @VedlegglistestørrelseConstraint List<@Valid VedleggDto> vedlegg) implements EndringssøknadDto {
}
