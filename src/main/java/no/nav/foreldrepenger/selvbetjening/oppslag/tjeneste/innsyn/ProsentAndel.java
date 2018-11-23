package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn;

import static java.lang.String.format;
import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonValue;

public class ProsentAndel {

    @JsonValue
    @Prosent
    private final Double prosent;

    public static ProsentAndel valueOf(String prosent) {
        return new ProsentAndel(Optional.ofNullable(prosent)
                .map(s -> s.replace("%", ""))
                .map(String::trim)
                .map(ProsentAndel::konverter)
                .orElseThrow(() -> new IllegalArgumentException("Prosentandel må være satt")));
    }

    public ProsentAndel(Double prosent) {
        this.prosent = avrund(prosent, 1);
    }

    private static Double avrund(Double value, int presisjon) {
        if (presisjon < 0) {
            throw new IllegalArgumentException(format("Presisjon må være positiv, var %s", presisjon));
        }
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(presisjon, HALF_UP);
            return bd.doubleValue();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(format("Kunne ikke konvertere %s til presisjon %", value, presisjon), e);
        }
    }

    private static Double konverter(String value) {
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(format("Kunne ikke formattere %s til double", value), e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [prosent=" + prosent + "]";
    }
}
