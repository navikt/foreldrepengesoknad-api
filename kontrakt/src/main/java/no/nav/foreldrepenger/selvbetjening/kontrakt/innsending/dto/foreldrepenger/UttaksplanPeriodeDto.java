package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;

// TODO: Rydd opp
public record UttaksplanPeriodeDto(@NotNull UttaksplanPeriodeDto.Type type,
                                   @Valid ÅpenPeriodeDto tidsperiode,
                                   KontoType konto,
                                   @Pattern(regexp = "^[\\p{Digit}\\p{L}_]*$") String morsAktivitetIPerioden,
                                   @Pattern(regexp = "^[\\p{Digit}\\p{L}_]*$") String årsak,
                                   @Min(0) @Max(100) Double samtidigUttakProsent,
                                   @Min(0) @Max(100) Double stillingsprosent,
                                   boolean erArbeidstaker,
                                   boolean erFrilanser,
                                   boolean erSelvstendig,
                                   boolean gradert,
                                   boolean ønskerFlerbarnsdager,
                                   boolean ønskerSamtidigUttak,
                                   Boolean justeresVedFødsel,
                                   @Valid @Size(max = 15) List<@Pattern(regexp = FRITEKST) String> orgnumre) {

    public UttaksplanPeriodeDto {
        orgnumre = Optional.ofNullable(orgnumre).orElse(List.of());
    }

    public enum Type {
        UTTAK,
        UTSETTELSE,
        OPPHOLD,
        OVERFØRING,
        PERIODEUTENUTTAK
    }

    public enum KontoType {
        FELLESPERIODE,
        MØDREKVOTE,
        FEDREKVOTE,
        FORELDREPENGER,
        FORELDREPENGER_FØR_FØDSEL
    }
}
