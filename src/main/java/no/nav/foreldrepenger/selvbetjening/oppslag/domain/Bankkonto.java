package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(NON_NULL)
public class Bankkonto {
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((banknavn == null) ? 0 : banknavn.hashCode());
        result = prime * result + ((kontonummer == null) ? 0 : kontonummer.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bankkonto other = (Bankkonto) obj;
        if (banknavn == null) {
            if (other.banknavn != null)
                return false;
        } else if (!banknavn.equals(other.banknavn))
            return false;
        if (kontonummer == null) {
            if (other.kontonummer != null)
                return false;
        } else if (!kontonummer.equals(other.kontonummer))
            return false;
        return true;
    }

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
