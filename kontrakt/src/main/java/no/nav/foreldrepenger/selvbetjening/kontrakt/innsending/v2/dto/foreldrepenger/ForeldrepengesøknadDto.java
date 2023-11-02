package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering.VedlegglistestørrelseConstraint;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.annenpart.AnnenForelderDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan.UttaksplanDto;

public record ForeldrepengesøknadDto(LocalDate mottattdato,
                                     @Valid @NotNull SøkerDto søker,
                                     @Valid @NotNull BarnDto barn,
                                     @Valid AnnenForelderDto annenForelder,
                                     @Valid @NotNull Dekningsgrad dekningsgrad,
                                     @Valid UtenlandsoppholdDto utenlandsopphold,
                                     @Valid @Size(max = 40) List<@Valid @NotNull UtenlandsoppholdsperiodeDto> oppholdIUtlandet,
                                     @Pattern(regexp = FRITEKST) String tilleggsopplysninger,
                                     @Valid @NotNull UttaksplanDto uttaksplan,
                                     @Valid @VedlegglistestørrelseConstraint @Size(max = 100) List<@Valid VedleggDto> vedlegg) implements SøknadDto {
    public ForeldrepengesøknadDto {
        vedlegg = Optional.ofNullable(vedlegg).map(ArrayList::new).orElse(new ArrayList<>());
    }


}
