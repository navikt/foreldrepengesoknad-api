package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(NON_EMPTY)
public class Kvittering {

    private final LocalDateTime mottattDato;
    private final LocalDate førsteDag;
    private final String referanseId;
    private final String leveranseStatus;
    private final String journalId;
    private final String saksNr;
    private final byte[] pdf;
    private final LocalDate førsteInntektsmeldingDag;
    private final byte[] infoskrivPdf;

    @JsonCreator
    public Kvittering(@JsonProperty("mottattDato") LocalDateTime mottattDato,
            @JsonProperty("referanseId") String referanseId,
            @JsonProperty("leveranseStatus") String leveranseStatus,
            @JsonProperty("journalId") String journalId,
            @JsonProperty("saksNr") String saksNr,
            @JsonProperty("pdf") byte[] pdf,
            @JsonProperty("infoskrivPdf") byte[] infoskrivPdf,
            @JsonProperty("førsteInntektsmeldingDag") LocalDate førsteInntektsmeldingDag,
            @JsonProperty("førsteDag") LocalDate førsteDag) {
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

    public LocalDateTime getMottattDato() {
        return mottattDato;
    }

    public LocalDate getFørsteDag() {
        return førsteDag;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public String getLeveranseStatus() {
        return leveranseStatus;
    }

    public String getJournalId() {
        return journalId;
    }

    public String getSaksNr() {
        return saksNr;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public LocalDate getFørsteInntektsmeldingDag() {
        return førsteInntektsmeldingDag;
    }

    public byte[] getInfoskrivPdf() {
        return infoskrivPdf;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[mottattDato=" + mottattDato + ", førsteDag=" + førsteDag
                + ", referanseId=" + referanseId + ", leveranseStatus=" + leveranseStatus + ", journalId=" + journalId
                + ", saksNr=" + saksNr + ", pdf=" + Arrays.toString(pdf) + ", førsteInntektsmeldingDag="
                + førsteInntektsmeldingDag + ", infoskrivPdf=" + Arrays.toString(infoskrivPdf) + "]";
    }

}
