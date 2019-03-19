package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Uttaksplan {
    private final SøknadsGrunnlag grunnlag;
    private final List<UttaksPeriode> perioder;

    @JsonCreator
    public Uttaksplan(@JsonProperty("grunnlag") SøknadsGrunnlag grunnlag,
            @JsonProperty("perioder") List<UttaksPeriode> perioder) {
        this.grunnlag = grunnlag;
        this.perioder = perioder;
    }

    public SøknadsGrunnlag getGrunnlag() {
        return grunnlag;
    }

    public List<UttaksPeriode> getPerioder() {
        return perioder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(grunnlag, perioder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Uttaksplan that = (Uttaksplan) o;
        return Objects.equals(grunnlag, that.grunnlag) && Objects.equals(this.perioder, that.perioder);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [grunnlag=" + grunnlag + ", perioder=" + perioder + "]";
    }
}
