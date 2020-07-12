package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProsentAndel {

    @Prosent
    private final Double prosent;

    @JsonCreator
    public ProsentAndel(@JsonProperty("prosent") Double prosent) {
        this.prosent = round(prosent, 1);
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

    public Double getProsent() {
        return prosent;
    }
}
