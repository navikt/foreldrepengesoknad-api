package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;

public final class MedlemsskapMaler {

    private MedlemsskapMaler() {
    }

    public static UtenlandsoppholdDto medlemsskapNorge() {
        return new UtenlandsoppholdDto(emptyList(), emptyList());
    }

    public static UtenlandsoppholdDto medlemskapUtlandetForrige12mnd() {
        return new UtenlandsoppholdDto(List.of(utenlandsopphold(LocalDate.now().minusYears(2), LocalDate.now())), emptyList());
    }

    private static UtenlandsoppholdPeriodeDto utenlandsopphold(LocalDate fom, LocalDate tom) {
        return new UtenlandsoppholdPeriodeDto(CountryCode.US.getAlpha2(), new ÅpenPeriodeDto(fom, tom));
    }
}
