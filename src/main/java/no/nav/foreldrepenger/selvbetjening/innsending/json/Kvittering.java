package no.nav.foreldrepenger.selvbetjening.innsending.json;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public class Kvittering {
    public static final Kvittering STUB = new Kvittering(now(),"deadbeef-69-cafebabe-42", "PÅGÅR", "12345", "67890");

    public LocalDateTime mottattDato;
    public String referanseId;
    public String leveranseStatus;
    public String journalId;
    public String saksNr;

    @SuppressWarnings("unused")
    public Kvittering() {}

    public Kvittering(LocalDateTime mottattDato, String referanseId, String leveranseStatus, String journalId, String saksNr) {
        this.mottattDato = mottattDato;
        this.referanseId = referanseId;
        this.leveranseStatus = leveranseStatus;
        this.journalId = journalId;
        this.saksNr = saksNr;
    }
}
