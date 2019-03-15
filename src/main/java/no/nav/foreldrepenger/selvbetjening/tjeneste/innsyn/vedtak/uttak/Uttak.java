package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Uttak {

    private final LocalDate førsteLovligeUttaksDato;
    private final List<UttaksPeriode> uttaksPerioder;

    @JsonCreator
    public Uttak(@JsonProperty("førsteLovligeUttaksDato") LocalDate førsteLovligeUttaksDato,
            @JsonProperty("uttaksPerioder") List<UttaksPeriode> uttaksPerioder) {
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
