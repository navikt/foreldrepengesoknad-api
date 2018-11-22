package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonValue;

public class ProsentAndel {

    @JsonValue
    @Prosent
    private final Double prosent;

    public ProsentAndel(Double prosent) {
        this.prosent = round(prosent, 1);
    }

    public static ProsentAndel valueOf(String prosent) {
        return new ProsentAndel(Optional.ofNullable(prosent)
                .map(s -> s.replace("%", ""))
                .map(String::trim)
                .map(Double::valueOf)
                .orElseThrow(() -> new IllegalArgumentException("Prosentandel må være satt")));
    }

    private static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [prosent=" + prosent + "]";
    }
}
