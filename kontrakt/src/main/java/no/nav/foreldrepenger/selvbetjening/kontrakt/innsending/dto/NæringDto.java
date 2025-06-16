package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import com.neovisionaries.i18n.CountryCode;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;

import java.time.LocalDate;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

public record NæringDto(@Valid @NotNull LocalDate fom,
                        @Valid LocalDate tom,
                        @Valid Virksomhetstype næringstype,
                        @Pattern(regexp = FRITEKST) String navnPåNæringen,
                        @Valid Orgnummer organisasjonsnummer,
                        @Digits(integer = 9, fraction = 0) Integer næringsinntekt,
                        boolean registrertINorge,
                        CountryCode registrertILand,
                        boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene,
                        LocalDate oppstartsdato,
                        boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår,
                        LocalDate varigEndringDato,
                        @Digits(integer = 9, fraction = 0) Integer varigEndringInntektEtterEndring,
                        @Pattern(regexp = FRITEKST) String varigEndringBeskrivelse) {

    @AssertTrue(message = "Søker har oppgitt varig endring men varigEndringDato, varigEndringInntektEtterEndring og varigEndringBeskrivelse er ikke"
        + " satt")
    public boolean isVarigEndringGyldig() {
        if (hattVarigEndringAvNæringsinntektSiste4Kalenderår) {
            return varigEndringDato != null && varigEndringInntektEtterEndring != null && varigEndringBeskrivelse != null;
        }
        return true;
    }
}
