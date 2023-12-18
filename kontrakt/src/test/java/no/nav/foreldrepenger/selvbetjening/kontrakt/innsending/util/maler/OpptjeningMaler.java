package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler;

import java.time.LocalDate;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FrilansInformasjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringsinntektInformasjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.TilknyttetPersonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;

public final class OpptjeningMaler {

    private OpptjeningMaler() {
    }

    public static FrilansInformasjonDto frilansOpptjening() {
        return new FrilansInformasjonDto(LocalDate.now().minusYears(2), false);
    }

    public static NæringDto egenNaeringOpptjening(String orgnummer) {
        return lagNorskOrganisasjon(orgnummer, LocalDate.now().minusYears(4), LocalDate.now(), false, 1_500_000, true);
    }

    public static NæringDto egenNaeringOpptjening(String orgnummer, Boolean erNyIArbeidslivet, double næringsInntekt, Boolean varigEndretNæring) {
        return lagNorskOrganisasjon(orgnummer, LocalDate.now().minusYears(4), LocalDate.now(), erNyIArbeidslivet, næringsInntekt, varigEndretNæring);
    }

    public static NæringDto egenNaeringOpptjening(String orgnummer, LocalDate fom, LocalDate tom, Boolean erNyIArbeidslivet,
                                                   Number næringsInntekt, Boolean varigEndretNæring) {
        return lagNorskOrganisasjon(orgnummer, fom, tom, erNyIArbeidslivet, næringsInntekt, varigEndretNæring);
    }

    public static AnnenInntektDto utenlandskArbeidsforhold(CountryCode landKode) {
        return lagUtenlandskArbeidsforhold(landKode, AnnenOpptjeningType.JOBB_I_UTLANDET);
    }

    public static AnnenInntektDto annenInntektNorsk(AnnenOpptjeningType type) {
        return lagUtenlandskArbeidsforhold(CountryCode.NO, type);
    }


    /* PRIVATE HJELPEMETODER */
    private static NæringDto lagNorskOrganisasjon(String orgnummer, LocalDate fom, LocalDate tom, Boolean erNyIArbeidslivet, Number næringsInntekt, Boolean varigEndretNæring) {
        var endringAvNæring = new NæringsinntektInformasjonDto(
                varigEndretNæring.equals(Boolean.TRUE) ? LocalDate.now().minusWeeks(1) : null,
                næringsInntekt.intValue(),
                "Endringsbeskrivelse"
        );
        return new NæringDto(
                erNyIArbeidslivet,
                varigEndretNæring,
                true,
                100.0,
                næringsInntekt.intValue(),
                List.of(Virksomhetstype.ANNEN),
                CountryCode.NO.getAlpha3(),
                List.of(),
                LocalDate.now().minusYears(4),
                endringAvNæring,
                "Navnet på Næring",
                orgnummer,
                new ÅpenPeriodeDto(fom, tom),
                new TilknyttetPersonDto("Regnar Regnskap", "99999999", false),
                null
        );

    }

    private static AnnenInntektDto lagUtenlandskArbeidsforhold(CountryCode landKode, AnnenOpptjeningType type) {
        return new AnnenInntektDto(
                type.name(),
                landKode.getAlpha2(),
                "elskap AS",
                new ÅpenPeriodeDto(LocalDate.now().minusYears(4), LocalDate.now()),
                false,
                List.of()
        );
    }

}
