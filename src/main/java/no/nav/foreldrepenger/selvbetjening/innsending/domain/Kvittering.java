package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Kvittering {

    private final LocalDateTime mottattDato;
    private final String saksNr;
    private final byte[] pdf;
    private final byte[] infoskrivPdf;

    @JsonCreator
    public Kvittering(@JsonProperty("mottattDato") LocalDateTime mottattDato,
            @JsonProperty("saksNr") String saksNr,
            @JsonProperty("pdf") byte[] pdf,
            @JsonProperty("infoskrivPdf") byte[] infoskrivPdf) {
        this.mottattDato = mottattDato;
        this.saksNr = saksNr;
        this.pdf = pdf;
        this.infoskrivPdf = infoskrivPdf;
    }

    public LocalDateTime getMottattDato() {
        return mottattDato;
    }

    public String getSaksNr() {
        return saksNr;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public byte[] getInfoskrivPdf() {
        return infoskrivPdf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Kvittering that = (Kvittering) o;
        return Objects.equals(mottattDato, that.mottattDato) && Objects.equals(saksNr, that.saksNr) && Arrays.equals(
            pdf, that.pdf) && Arrays.equals(infoskrivPdf, that.infoskrivPdf);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mottattDato, saksNr);
        result = 31 * result + Arrays.hashCode(pdf);
        result = 31 * result + Arrays.hashCode(infoskrivPdf);
        return result;
    }

    @Override
    public String toString() {
        return "Kvittering{" + "mottattDato=" + mottattDato + ", saksNr='" + saksNr + '\'' + '}';
    }
}
