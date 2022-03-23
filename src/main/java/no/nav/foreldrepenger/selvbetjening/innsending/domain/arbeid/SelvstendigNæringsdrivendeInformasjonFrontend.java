package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

public record SelvstendigNæringsdrivendeInformasjonFrontend(
    boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene,
    boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår,
    boolean registrertINorge,
    Double stillingsprosent,
    int næringsinntekt,
    List<@Pattern(regexp = FRITEKST) String> næringstyper,
    List<@Pattern(regexp = FRITEKST) String> vedlegg,
    LocalDate oppstartsdato,
    @Valid NæringsinntektInformasjonFrontend endringAvNæringsinntektInformasjon,
    @Pattern(regexp = FRITEKST) String navnPåNæringen,
    @Pattern(regexp = FRITEKST) String organisasjonsnummer,
    @Pattern(regexp = FRITEKST) String registrertILand,
    Tidsperiode tidsperiode,
    @Valid TilknyttetPerson regnskapsfører,
    @Valid TilknyttetPerson revisor) {
}
