package no.nav.foreldrepenger.selvbetjening.innsyn.vedtak.uttak;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonValue;

public class ProsentAndel {

    @JsonValue
    @Prosent
    private final Double prosent;

    public static ProsentAndel of(Integer prosent) {
        return new ProsentAndel(prosent.doubleValue());
    }

    public ProsentAndel(Double prosent) {
        this.prosent = round(prosent, 1);
    }

    public static ProsentAndel valueOf(String prosent) {
        return new ProsentAndel(tilProsent(prosent));
    }

    private static Double tilProsent(String prosent) {
        return Optional.ofNullable(prosent)
                .map(s -> s.replace("%", ""))
                .map(String::trim)
                .map(Double::valueOf)
                .orElseThrow(() -> new IllegalArgumentException("Prosentandel må være satt"));
    }

    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [prosent=" + prosent + "]";
    }
}
