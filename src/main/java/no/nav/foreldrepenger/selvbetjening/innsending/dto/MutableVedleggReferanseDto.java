package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public final class MutableVedleggReferanseDto {

    @JsonValue
    private @NotNull @Pattern(regexp = "^[\\p{Digit}\\p{L}-_]*$") String referanse;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public MutableVedleggReferanseDto(@NotNull @Pattern(regexp = "^[\\p{Digit}\\p{L}-_]*$") String referanse) {
        this.referanse = referanse;
    }

    public String referanse() {
        return referanse;
    }

    public void referanse(String referanse) {
        this.referanse = referanse;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MutableVedleggReferanseDto) obj;
        return Objects.equals(this.referanse, that.referanse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referanse);
    }

    @Override
    public String toString() {
        return "MutableVedleggReferanse[" +
                "referanse=" + referanse + ']';
    }

}
