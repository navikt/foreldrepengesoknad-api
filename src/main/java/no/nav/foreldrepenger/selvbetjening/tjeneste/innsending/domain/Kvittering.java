package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.time.LocalDateTime.now;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public class Kvittering {
    public static final Kvittering STUB = new Kvittering(now(), "deadbeef-69-cafebabe-42", "PÅGÅR", "12345", "67890",
            new byte[0], new byte[0], LocalDate.now(), LocalDate.now());

    public LocalDateTime mottattDato;
    public LocalDate førsteDag;
    public String referanseId;
    public String leveranseStatus;
    public String journalId;
    public String saksNr;
    public byte[] pdf;
    public LocalDate førsteInntektsmeldingDag;
    public byte[] infoskrivPdf;

    @SuppressWarnings("unused")
    public Kvittering() {
    }

    public Kvittering(LocalDateTime mottattDato, String referanseId, String leveranseStatus, String journalId,
            String saksNr, byte[] pdf, byte[] infoskrivPdf, LocalDate førsteInntektsmeldingDag, LocalDate førsteDag) {
        this.mottattDato = mottattDato;
        this.referanseId = referanseId;
        this.leveranseStatus = leveranseStatus;
        this.journalId = journalId;
        this.saksNr = saksNr;
        this.pdf = pdf;
        this.infoskrivPdf = infoskrivPdf;
        this.førsteInntektsmeldingDag = førsteInntektsmeldingDag;
        this.førsteDag = førsteDag;
    }
}
