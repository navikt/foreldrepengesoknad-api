package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

public record Bankkonto(String kontonummer, String banknavn) {

    @Override
    public String toString() {
        return "Bankkonto{kontonummer=***, banknavn=***}";
    }
}
