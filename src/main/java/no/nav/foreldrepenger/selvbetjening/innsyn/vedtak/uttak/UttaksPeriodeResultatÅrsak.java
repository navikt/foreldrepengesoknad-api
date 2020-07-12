package no.nav.foreldrepenger.selvbetjening.innsyn.vedtak.uttak;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UttaksPeriodeResultatÅrsak {

    private final String tekst;

    public String getTekst() {
        return tekst;
    }

    @JsonCreator
    public UttaksPeriodeResultatÅrsak(@JsonProperty("tekst") String tekst) {
        this.tekst = tekst;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tekst=" + tekst + "]";
    }

}
