package no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

public record Uttaksplan(SøknadsGrunnlag grunnlag, List<UttaksPeriode> perioder) {

    public Uttaksplan {
        perioder = Optional.ofNullable(perioder).orElse(emptyList());
    }
}
