package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering.VedlegglistestørrelseConstraint;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

@Deprecated
public record ForeldrepengesøknadDtoOLD(LocalDate mottattdato, @NotNull SituasjonOLD situasjon,
                                        @Valid @NotNull SøkerDtoOLD søker,
                                        @Valid @NotNull BarnDtoOLD barn,
                                        @Valid @NotNull AnnenforelderDtoOLD annenForelder,
                                        @Valid @NotNull DekningsgradOLD dekningsgrad,
                                        @Pattern(regexp = FRITEKST) String tilleggsopplysninger,
                                        @Valid @NotNull UtenlandsoppholdDtoOLD informasjonOmUtenlandsopphold,
                                        @Valid @Size(max = 200) List<@Valid @NotNull UttaksplanPeriodeDtoOLD> uttaksplan,
                                        Boolean ønskerJustertUttakVedFødsel,
                                        @Valid @VedlegglistestørrelseConstraint @Size(max = 100) List<@Valid VedleggDto> vedlegg) implements SøknadDtoOLD {
    public ForeldrepengesøknadDtoOLD {
        uttaksplan = Optional.ofNullable(uttaksplan).orElse(List.of());
        vedlegg = Optional.ofNullable(vedlegg).orElse(List.of());
    }
}
