package no.nav.foreldrepenger.selvbetjening.rest.json;

import java.time.LocalDateTime;

public class Kvittering {
    public static final Kvittering STUB = new Kvittering("0", LocalDateTime.now());
    public final LocalDateTime mottattDato;
    public final String referanseId;

    public Kvittering(String referanseId, LocalDateTime mottattDato) {
        this.referanseId = referanseId;
        this.mottattDato = mottattDato;
    }
}
