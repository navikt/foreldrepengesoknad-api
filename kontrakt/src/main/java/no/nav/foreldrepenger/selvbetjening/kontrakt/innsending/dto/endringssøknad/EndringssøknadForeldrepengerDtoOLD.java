package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.AnnenforelderDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.SituasjonOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering.VedlegglistestørrelseConstraint;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

@Deprecated
public record EndringssøknadForeldrepengerDtoOLD(LocalDate mottattdato, @NotNull SituasjonOLD situasjon, @Valid @NotNull Saksnummer saksnummer,
                                                 @Valid @NotNull SøkerDtoOLD søker, @Valid @NotNull BarnDtoOLD barn,
                                                 @Valid @NotNull AnnenforelderDtoOLD annenForelder,
                                                 @Pattern(regexp = FRITEKST) String tilleggsopplysninger, Boolean ønskerJustertUttakVedFødsel,
                                                 @Valid @Size(max = 200) List<@Valid @NotNull UttaksplanPeriodeDtoOLD> uttaksplan,
                                                 @Valid @VedlegglistestørrelseConstraint @Size(max = 100) List<@Valid VedleggDto> vedlegg) implements EndringssøknadDtoOLD {

    public EndringssøknadForeldrepengerDtoOLD {
        uttaksplan = Optional.ofNullable(uttaksplan).orElse(List.of());
        vedlegg = Optional.ofNullable(vedlegg).orElse(List.of());
    }
}
