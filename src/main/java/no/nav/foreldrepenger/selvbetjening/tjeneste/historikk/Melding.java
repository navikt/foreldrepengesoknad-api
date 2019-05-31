package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.time.LocalDate;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;

public class Melding {

    private final AktørId aktørId;
    private final String melding;
    private final String saknr;
    private LocalDate dato;

    public Melding(AktørId aktørId, String melding, String saksnr) {
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
