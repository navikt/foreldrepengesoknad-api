package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
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
