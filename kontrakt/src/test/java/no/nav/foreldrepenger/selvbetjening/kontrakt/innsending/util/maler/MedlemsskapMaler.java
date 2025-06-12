package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdPeriodeDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDtoOLD;

public final class MedlemsskapMaler {

    private MedlemsskapMaler() {
    }

    public static UtenlandsoppholdDtoOLD medlemsskapNorge() {
        return new UtenlandsoppholdDtoOLD(emptyList(), emptyList());
    }

    public static UtenlandsoppholdDtoOLD medlemskapUtlandetForrige12mnd() {
        return new UtenlandsoppholdDtoOLD(List.of(utenlandsopphold(LocalDate.now().minusYears(2), LocalDate.now())), emptyList());
    }

    private static UtenlandsoppholdPeriodeDtoOLD utenlandsopphold(LocalDate fom, LocalDate tom) {
        return new UtenlandsoppholdPeriodeDtoOLD(CountryCode.US.getAlpha2(), new ÅpenPeriodeDtoOLD(fom, tom));
    }
}
