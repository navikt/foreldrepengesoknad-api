package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;

public class Melding {

    private final AktørId aktørId;
    private final String melding;
    private final String saknr;
    private LocalDate dato;

    @JsonCreator
    public Melding(@JsonProperty("aktørId") AktørId aktørId, @JsonProperty("melding") String melding,
            @JsonProperty("saksnr") String saksnr) {
        this.aktørId = aktørId;
        this.melding = melding;
        this.saknr = saksnr;
    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public String getMelding() {
        return melding;
    }

    public String getSaknr() {
        return saknr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [aktørId=" + aktørId + ", melding=" + melding + ", saknr=" + saknr
                + ", dato=" + dato + "]";
    }

}
