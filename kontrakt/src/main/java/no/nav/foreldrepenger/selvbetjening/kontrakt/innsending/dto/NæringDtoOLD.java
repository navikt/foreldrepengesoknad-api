package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.ORGNUMMER;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;

public record NæringDtoOLD(boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene, boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår,
                           boolean registrertINorge, @Digits(integer = 3, fraction = 2) Double stillingsprosent,
                           @Digits(integer = 9, fraction = 0) int næringsinntekt, @Valid @Size(max = 10) List<Virksomhetstype> næringstyper,
                           @Pattern(regexp = BARE_BOKSTAVER) String registrertILand, LocalDate oppstartsdato,
                           @Valid NæringsinntektInformasjonDtoOLD endringAvNæringsinntektInformasjon,
                           @Pattern(regexp = FRITEKST) String navnPåNæringen, @Pattern(regexp = ORGNUMMER) String organisasjonsnummer,
                           @Valid ÅpenPeriodeDtoOLD tidsperiode, @Valid TilknyttetPersonDtoOLD regnskapsfører,
                           @Valid TilknyttetPersonDtoOLD revisor) {
    public NæringDtoOLD {
        næringstyper = Optional.ofNullable(næringstyper).orElse(emptyList());
    }
}
