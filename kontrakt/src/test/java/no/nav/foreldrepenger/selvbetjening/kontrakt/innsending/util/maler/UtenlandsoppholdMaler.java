package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdDto;

public final class UtenlandsoppholdMaler {

    private UtenlandsoppholdMaler() {
    }

    public static UtenlandsoppholdDto oppholdBareINorge() {
        return new UtenlandsoppholdDto(emptyList(), emptyList());
    }

    public static UtenlandsoppholdDto oppholdIUtlandetForrige12mnd() {
        return new UtenlandsoppholdDto(
            List.of(new UtenlandsoppholdDto.Periode(LocalDate.now().minusYears(2), LocalDate.now(), CountryCode.US)),
            emptyList()
        );
    }
}
