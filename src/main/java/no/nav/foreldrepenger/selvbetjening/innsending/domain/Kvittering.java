package no.nav.foreldrepenger.selvbetjening.innsending.domain;

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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((førsteDag == null) ? 0 : førsteDag.hashCode());
        result = prime * result + ((førsteInntektsmeldingDag == null) ? 0 : førsteInntektsmeldingDag.hashCode());
        result = prime * result + Arrays.hashCode(infoskrivPdf);
        result = prime * result + ((journalId == null) ? 0 : journalId.hashCode());
        result = prime * result + ((leveranseStatus == null) ? 0 : leveranseStatus.hashCode());
        result = prime * result + ((mottattDato == null) ? 0 : mottattDato.hashCode());
        result = prime * result + Arrays.hashCode(pdf);
        result = prime * result + ((referanseId == null) ? 0 : referanseId.hashCode());
        result = prime * result + ((saksNr == null) ? 0 : saksNr.hashCode());
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
        Kvittering other = (Kvittering) obj;
        if (førsteDag == null) {
            if (other.førsteDag != null)
                return false;
        } else if (!førsteDag.equals(other.førsteDag))
            return false;
        if (førsteInntektsmeldingDag == null) {
            if (other.førsteInntektsmeldingDag != null)
                return false;
        } else if (!førsteInntektsmeldingDag.equals(other.førsteInntektsmeldingDag))
            return false;
        if (!Arrays.equals(infoskrivPdf, other.infoskrivPdf))
            return false;
        if (journalId == null) {
            if (other.journalId != null)
                return false;
        } else if (!journalId.equals(other.journalId))
            return false;
        if (leveranseStatus == null) {
            if (other.leveranseStatus != null)
                return false;
        } else if (!leveranseStatus.equals(other.leveranseStatus))
            return false;
        if (mottattDato == null) {
            if (other.mottattDato != null)
                return false;
        } else if (!mottattDato.equals(other.mottattDato))
            return false;
        if (!Arrays.equals(pdf, other.pdf))
            return false;
        if (referanseId == null) {
            if (other.referanseId != null)
                return false;
        } else if (!referanseId.equals(other.referanseId))
            return false;
        if (saksNr == null) {
            if (other.saksNr != null)
                return false;
        } else if (!saksNr.equals(other.saksNr))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[mottattDato=" + mottattDato + ", førsteDag=" + førsteDag
                + ", referanseId=" + referanseId + ", leveranseStatus=" + leveranseStatus + ", journalId=" + journalId
                + ", saksNr=" + saksNr + ", førsteInntektsmeldingDag="
                + førsteInntektsmeldingDag + "]";
    }

}
