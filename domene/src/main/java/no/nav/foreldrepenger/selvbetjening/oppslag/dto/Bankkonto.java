package no.nav.foreldrepenger.selvbetjening.oppslag.dto;

public record Bankkonto(String kontonummer, String banknavn) {

    @Override
    public String toString() {
        return "Bankkonto{kontonummer=***, banknavn=***}";
    }
}
