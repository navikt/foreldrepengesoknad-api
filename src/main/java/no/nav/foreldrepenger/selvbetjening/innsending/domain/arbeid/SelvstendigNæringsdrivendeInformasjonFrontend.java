package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.ORGNUMMER;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

public record SelvstendigNæringsdrivendeInformasjonFrontend(
    boolean harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene,
    boolean hattVarigEndringAvNæringsinntektSiste4Kalenderår,
    boolean registrertINorge,
    @Digits(integer = 3, fraction = 2) Double stillingsprosent,
    @Digits(integer = 9, fraction = 0) int næringsinntekt,
    @Valid @Size(max = 10) List<Virksomhetstype> næringstyper,
    @Valid @Size(max = 15) List<VedleggReferanse> vedlegg,
    LocalDate oppstartsdato,
    @Valid NæringsinntektInformasjonFrontend endringAvNæringsinntektInformasjon,
    @Pattern(regexp = FRITEKST) String navnPåNæringen,
    @Pattern(regexp = ORGNUMMER) String organisasjonsnummer,
    @Pattern(regexp = BARE_BOKSTAVER) String registrertILand,
    @Valid Tidsperiode tidsperiode,
    @Valid TilknyttetPerson regnskapsfører,
    @Valid TilknyttetPerson revisor) {
}
