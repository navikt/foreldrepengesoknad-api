package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.ORGNUMMER;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

public record SelvstendigNæringsdrivendeInformasjonFrontend(
    boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene,
    boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår,
    boolean registrertINorge,
    Double stillingsprosent,
    int næringsinntekt,
    List<Virksomhetstype> næringstyper,
    List<@Pattern(regexp = "^[\\p{Digit}\\p{L}]*$") String> vedlegg,
    LocalDate oppstartsdato,
    @Valid NæringsinntektInformasjonFrontend endringAvNæringsinntektInformasjon,
    @Pattern(regexp = FRITEKST) String navnPåNæringen,
    @Pattern(regexp = ORGNUMMER) String organisasjonsnummer,
    @Pattern(regexp = BARE_BOKSTAVER) String registrertILand,
    Tidsperiode tidsperiode,
    @Valid TilknyttetPerson regnskapsfører,
    @Valid TilknyttetPerson revisor) {
}
