package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Kvittering {
    public static final Kvittering STUB = new Kvittering("0", LocalDateTime.now());
    public final LocalDateTime mottattDato;
    public final String referanseId;

    @JsonCreator
    public Kvittering(@JsonProperty("referanseId") String referanseId,
            @JsonProperty("mottattDato") LocalDateTime mottattDato) {
        this.referanseId = referanseId;
        this.mottattDato = mottattDato;
    }
}
