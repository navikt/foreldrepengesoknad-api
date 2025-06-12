package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler;

import java.time.LocalDate;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AnnenInntektDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FrilansInformasjonDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringsinntektInformasjonDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.TilknyttetPersonDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDtoOLD;

public final class OpptjeningMaler {

    private OpptjeningMaler() {
    }

    public static FrilansInformasjonDtoOLD frilansOpptjening() {
        return new FrilansInformasjonDtoOLD(LocalDate.now().minusYears(2), false);
    }

    public static NæringDtoOLD egenNaeringOpptjening(String orgnummer) {
        return lagNorskOrganisasjon(orgnummer, LocalDate.now().minusYears(4), LocalDate.now(), false, 1_500_000, true);
    }

    public static NæringDtoOLD egenNaeringOpptjening(String orgnummer, Boolean erNyIArbeidslivet, double næringsInntekt, Boolean varigEndretNæring) {
        return lagNorskOrganisasjon(orgnummer, LocalDate.now().minusYears(4), LocalDate.now(), erNyIArbeidslivet, næringsInntekt, varigEndretNæring);
    }

    public static NæringDtoOLD egenNaeringOpptjening(String orgnummer,
                                                     LocalDate fom,
                                                     LocalDate tom,
                                                     Boolean erNyIArbeidslivet,
                                                     Number næringsInntekt,
                                                     Boolean varigEndretNæring) {
        return lagNorskOrganisasjon(orgnummer, fom, tom, erNyIArbeidslivet, næringsInntekt, varigEndretNæring);
    }

    public static AnnenInntektDtoOLD utenlandskArbeidsforhold(CountryCode landKode) {
        return lagUtenlandskArbeidsforhold(landKode, AnnenOpptjeningType.JOBB_I_UTLANDET,
            new ÅpenPeriodeDtoOLD(LocalDate.now().minusYears(4), LocalDate.now()));
    }

    public static AnnenInntektDtoOLD annenInntektNorsk(AnnenOpptjeningType type) {
        return lagUtenlandskArbeidsforhold(CountryCode.NO, type, new ÅpenPeriodeDtoOLD(LocalDate.now().minusYears(4), LocalDate.now()));
    }

    public static AnnenInntektDtoOLD annenInntektNorsk(AnnenOpptjeningType type, ÅpenPeriodeDtoOLD periode) {
        return lagUtenlandskArbeidsforhold(CountryCode.NO, type, periode);
    }


    /* PRIVATE HJELPEMETODER */
    private static NæringDtoOLD lagNorskOrganisasjon(String orgnummer,
                                                     LocalDate fom,
                                                     LocalDate tom,
                                                     Boolean erNyIArbeidslivet,
                                                     Number næringsInntekt,
                                                     Boolean varigEndretNæring) {
        var endringAvNæring = new NæringsinntektInformasjonDtoOLD(varigEndretNæring.equals(Boolean.TRUE) ? LocalDate.now().minusWeeks(1) : null,
            næringsInntekt.intValue(), "Endringsbeskrivelse");
        return new NæringDtoOLD(erNyIArbeidslivet, varigEndretNæring, true, 100.0, næringsInntekt.intValue(), List.of(Virksomhetstype.ANNEN),
            CountryCode.NO.getAlpha3(), LocalDate.now().minusYears(4), endringAvNæring, "Navnet på Næring", orgnummer,
            new ÅpenPeriodeDtoOLD(fom, tom), new TilknyttetPersonDtoOLD("Regnar Regnskap", "99999999", false), null);

    }

    private static AnnenInntektDtoOLD lagUtenlandskArbeidsforhold(CountryCode landKode, AnnenOpptjeningType type, ÅpenPeriodeDtoOLD periode) {
        return new AnnenInntektDtoOLD(type.name(), landKode.getAlpha2(), "elskap AS", periode, false);
    }

}
