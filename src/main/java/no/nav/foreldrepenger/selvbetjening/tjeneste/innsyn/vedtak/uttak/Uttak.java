package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak;

import java.time.LocalDate;
import java.util.List;

public class Uttak {

    private final LocalDate førsteLovligeUttaksDato;
    private final List<UttaksPeriode> uttaksPerioder;

    public Uttak(LocalDate førsteLovligeUttaksDato, List<UttaksPeriode> uttaksPerioder) {
        this.førsteLovligeUttaksDato = førsteLovligeUttaksDato;
        this.uttaksPerioder = uttaksPerioder;
    }

    public List<UttaksPeriode> getUttaksPerioder() {
        return uttaksPerioder;
    }

    public LocalDate getFørsteLovligeUttaksDato() {
        return førsteLovligeUttaksDato;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [førsteLovligeUttaksDato=" + førsteLovligeUttaksDato + ", uttaksPerioder="
                + uttaksPerioder + "]";
    }
}
