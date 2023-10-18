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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;

public record NæringDto(boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene,
                        boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår,
                        boolean registrertINorge,
                        @Digits(integer = 3, fraction = 2) Double stillingsprosent,
                        @Digits(integer = 9, fraction = 0) int næringsinntekt,
                        @Size(max = 10) List<Virksomhetstype> næringstyper,
                        @Pattern(regexp = BARE_BOKSTAVER) String registrertILand,
                        @Size(max = 15) List<@Valid MutableVedleggReferanseDto> vedlegg,
                        LocalDate oppstartsdato,
                        @Valid NæringsinntektInformasjonDto endringAvNæringsinntektInformasjon,
                        @NotNull @Pattern(regexp = FRITEKST) String navnPåNæringen,
                        @Pattern(regexp = ORGNUMMER) String organisasjonsnummer,
                        @Valid ÅpenPeriodeDto tidsperiode,
                        @Valid TilknyttetPersonDto regnskapsfører,
                        @Valid TilknyttetPersonDto revisor) {
    public NæringDto {
        næringstyper = Optional.ofNullable(næringstyper).orElse(emptyList());
        vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
    }
}
