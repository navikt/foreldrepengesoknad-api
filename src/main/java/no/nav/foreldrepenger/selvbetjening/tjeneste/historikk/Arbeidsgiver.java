package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

public class Arbeidsgiver {
    private String navn;
    private String orgnr;

    private Arbeidsgiver() {

    }

    public Arbeidsgiver(String navn, String orgnr) {
        this.navn = navn;
        this.orgnr = orgnr;
    }

    public String getNavn() {
        return navn;
    }

    public String getOrgnr() {
        return orgnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[navn=" + navn + ", orgnr=" + orgnr + "]";
    }
}
