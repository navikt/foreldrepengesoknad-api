package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static java.time.LocalDateTime.now;

public class Kvittering {
    public static final Kvittering STUB = new Kvittering("69", now());

    public final LocalDateTime mottattDato;
    public final String referanseId;

    @JsonCreator
    public Kvittering(@JsonProperty("referanseId") String referanseId,
            @JsonProperty("mottattDato") LocalDateTime mottattDato) {
        this.referanseId = referanseId;
        this.mottattDato = mottattDato;
    }
}
