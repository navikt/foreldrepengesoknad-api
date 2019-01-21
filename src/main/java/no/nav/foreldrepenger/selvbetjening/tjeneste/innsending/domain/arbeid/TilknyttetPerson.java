package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.arbeid;

public class TilknyttetPerson {

    public String navn;
    public String telefonnummer;
    public Boolean erNærVennEllerFamilie;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[navn=" + navn + ", telefonnummer=" + telefonnummer
                + ", erNærVennEllerFamilie="
                + erNærVennEllerFamilie + "]";
    }
}
