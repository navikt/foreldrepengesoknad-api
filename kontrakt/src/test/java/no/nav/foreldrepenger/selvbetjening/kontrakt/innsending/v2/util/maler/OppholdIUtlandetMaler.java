package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler;

import java.time.LocalDate;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;

public final class OppholdIUtlandetMaler {

    private OppholdIUtlandetMaler() {
    }

    public static List<UtenlandsoppholdsperiodeDto> oppholdBareINorge() {
        return List.of();
    }

    public static UtenlandsoppholdsperiodeDto oppholdIUtlandetForrige12mnd() {
        return new UtenlandsoppholdsperiodeDto(LocalDate.now().minusYears(2), LocalDate.now(), CountryCode.US);
    }
}
