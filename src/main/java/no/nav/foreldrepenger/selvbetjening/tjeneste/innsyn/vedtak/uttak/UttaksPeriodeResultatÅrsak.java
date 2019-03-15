package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak;

public class UttaksPeriodeResultatÅrsak {

    private final String tekst;

    public String getTekst() {
        return tekst;
    }

    public UttaksPeriodeResultatÅrsak(String tekst) {
        this.tekst = tekst;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tekst=" + tekst + "]";
    }

}
