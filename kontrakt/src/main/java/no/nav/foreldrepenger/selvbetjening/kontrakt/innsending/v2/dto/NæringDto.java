package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;

import com.neovisionaries.i18n.CountryCode;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;

public record NæringDto(@Valid @NotNull LocalDate fom,
                        @Valid LocalDate tom,
                        @Valid Virksomhetstype næringstype,
                        @Pattern(regexp = FRITEKST) String navnPåNæringen,
                        @Valid Orgnummer organisasjonsnummer,
                        @Digits(integer = 9, fraction = 0) int næringsinntekt,
                        boolean registrertINorge,
                        CountryCode registrertILand,
                        boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene,
                        LocalDate oppstartsdato,
                        boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår,
                        LocalDate varigEndringDato,
                        @Digits(integer = 9, fraction = 0) int varigEndringInntektEtterEndring,
                        @Pattern(regexp = FRITEKST) String varigEndringBeskrivelse) {
}
