package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler;

import java.time.LocalDate;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.UtenlandsoppholdsperiodeDto;

public final class UtenlandsoppholdMaler {

    private UtenlandsoppholdMaler() {
    }

    public static List<UtenlandsoppholdsperiodeDto> oppholdBareINorge() {
        return List.of();
    }

    public static List<UtenlandsoppholdsperiodeDto> oppholdIUtlandetForrige12mnd() {
        return List.of(new UtenlandsoppholdsperiodeDto(LocalDate.now().minusYears(2), LocalDate.now(), CountryCode.US));
    }
}
