package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(NON_NULL)
public class Bankkonto {
    private final String kontonummer;
    private final String banknavn;

    @JsonCreator
    public Bankkonto(@JsonProperty("kontonummer") String kontonummer, @JsonProperty("banknavn") String banknavn) {
        this.kontonummer = kontonummer;
        this.banknavn = banknavn;
    }

    public String getKontonummer() {
        return kontonummer;
    }

    public String getBanknavn() {
        return banknavn;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[kontonummer=" + kontonummer + ", banknavn=" + banknavn + "]";
    }
}
