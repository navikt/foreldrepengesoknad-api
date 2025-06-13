package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler;

import java.time.LocalDate;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.opptjening.AnnenOpptjeningType;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FrilansDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.NæringDto;

public final class OpptjeningMaler {

    private OpptjeningMaler() {
    }

    public static FrilansDto frilansOpptjening() {
        return new FrilansDto(false, LocalDate.now().minusYears(2));
    }

    public static NæringDto egenNaeringOpptjening(String orgnummer) {
        return lagNorskOrganisasjon(orgnummer, LocalDate.now().minusYears(4), LocalDate.now(), false, 1_500_000, true);
    }

    public static NæringDto egenNaeringOpptjening(String orgnummer, Boolean erNyIArbeidslivet, double næringsInntekt, Boolean varigEndretNæring) {
        return lagNorskOrganisasjon(orgnummer, LocalDate.now().minusYears(4), LocalDate.now(), erNyIArbeidslivet, næringsInntekt, varigEndretNæring);
    }

    public static NæringDto egenNaeringOpptjening(String orgnummer,
                                                  LocalDate fom,
                                                  LocalDate tom,
                                                  Boolean erNyIArbeidslivet,
                                                  Number næringsInntekt,
                                                  Boolean varigEndretNæring) {
        return lagNorskOrganisasjon(orgnummer, fom, tom, erNyIArbeidslivet, næringsInntekt, varigEndretNæring);
    }

    public static AnnenInntektDto utenlandskArbeidsforhold(CountryCode landKode) {
        return annenInntekt(AnnenOpptjeningType.JOBB_I_UTLANDET, landKode, LocalDate.now().minusYears(4), LocalDate.now());
    }

    public static AnnenInntektDto annenInntektNorsk(AnnenOpptjeningType type) {
        return annenInntekt(type, CountryCode.NO, LocalDate.now().minusYears(4), LocalDate.now());
    }

    public static AnnenInntektDto annenInntektNorsk(AnnenOpptjeningType type, LocalDate fom, LocalDate tom) {
        return annenInntekt(type, CountryCode.NO, fom, tom);
    }


    /* PRIVATE HJELPEMETODER */
    private static NæringDto lagNorskOrganisasjon(String orgnummer,
                                                  LocalDate fom,
                                                  LocalDate tom,
                                                  boolean erNyIArbeidslivet,
                                                  Number næringsInntekt,
                                                  Boolean varigEndretNæring) {
        return new NæringDto(fom,
            tom,
            Virksomhetstype.ANNEN,
            "Navnet på Næring",
            new Orgnummer(orgnummer),
            næringsInntekt.intValue(),
            true,
            CountryCode.NO,
            erNyIArbeidslivet,
            LocalDate.now().minusYears(4),
            varigEndretNæring,
            varigEndretNæring.equals(Boolean.TRUE) ? LocalDate.now().minusWeeks(1) : null,
            varigEndretNæring.equals(Boolean.TRUE) ? næringsInntekt.intValue() : null,
            varigEndretNæring.equals(Boolean.TRUE) ? "Endringsbeskrivelse" : null);

    }

    private static AnnenInntektDto annenInntekt(AnnenOpptjeningType type, CountryCode landKode, LocalDate fom, LocalDate tom) {
        if (AnnenOpptjeningType.JOBB_I_UTLANDET.equals(type)) {
            return new AnnenInntektDto(type, fom, tom, landKode, "Utenlandsk arbeidsgiver AS");
        }
        return new AnnenInntektDto(type, fom, tom, null, null);
    }

}
