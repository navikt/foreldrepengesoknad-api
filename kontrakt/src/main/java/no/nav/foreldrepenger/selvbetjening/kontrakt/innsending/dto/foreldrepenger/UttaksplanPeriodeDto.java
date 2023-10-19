package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;

import java.util.List;
import java.util.Optional;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

// TODO: Rydd opp
public record UttaksplanPeriodeDto(@NotNull UttaksplanPeriodeDto.Type type,
                                   @Valid ÅpenPeriodeDto tidsperiode,
                                   @Pattern(regexp = BARE_BOKSTAVER) String forelder,
                                   StønadskontoType konto,
                                   @Pattern(regexp = "^[\\p{Digit}\\p{L}_]*$") String morsAktivitetIPerioden,
                                   @Pattern(regexp = "^[\\p{Digit}\\p{L}_]*$") String årsak,
                                   Double samtidigUttakProsent,
                                   Double stillingsprosent,
                                   boolean erArbeidstaker,
                                   boolean erFrilanser,
                                   boolean erSelvstendig,
                                   boolean gradert,
                                   boolean ønskerFlerbarnsdager,
                                   boolean ønskerSamtidigUttak,
                                   Boolean justeresVedFødsel,
                                   List<@Pattern(regexp = FRITEKST) String> orgnumre,
                                   List<@Valid MutableVedleggReferanseDto> vedlegg) {

    public UttaksplanPeriodeDto {
        orgnumre = Optional.ofNullable(orgnumre).orElse(List.of());
        vedlegg = Optional.ofNullable(vedlegg).orElse(List.of());
    }

    public enum Type {
        UTTAK,
        UTSETTELSE,
        OPPHOLD,
        OVERFØRING,
        PERIODEUTENUTTAK
    }
}