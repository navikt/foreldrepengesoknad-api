package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.MorsAktivitet;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

public record GradertUttaksPeriodeDto(@NotNull LocalDate fom,
                                      @NotNull LocalDate tom,
                                      @NotNull KontoType konto,
                                      MorsAktivitet morsAktivitetIPerioden,
                                      Boolean ønskerSamtidigUttak,
                                      Boolean justeresVedFødsel,
                                      Boolean ønskerFlerbarnsdager,
                                      @Min(0) @Max(100) Double samtidigUttakProsent,

                                      @NotNull @Min(0) @Max(100) Double stillingsprosent,
                                      Boolean erArbeidstaker,
                                      Boolean erFrilanser,
                                      Boolean erSelvstendig,
                                      @Valid @Size(max = 15) List<@Pattern(regexp = FRITEKST) String> orgnumre,
                                      @Valid @Size(max = 100) List<@Valid @NotNull MutableVedleggReferanseDto> vedleggsreferanser) implements Uttaksplanperiode {
    public GradertUttaksPeriodeDto {
        vedleggsreferanser = Optional.ofNullable(vedleggsreferanser).map(ArrayList::new).orElse(new ArrayList<>());
    }
}