package no.nav.foreldrepenger.selvbetjening.oppslag.json;

public class Bankkonto {
    public String kontonummer;
    public String banknavn;

    public Bankkonto() {
    }

    public Bankkonto(String kontonummer, String banknavn) {
        this.kontonummer = kontonummer;
        this.banknavn = banknavn;
    }
}
