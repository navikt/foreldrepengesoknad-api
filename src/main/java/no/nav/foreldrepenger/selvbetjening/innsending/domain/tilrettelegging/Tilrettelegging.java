package no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public class Tilrettelegging {

    private String type;
    private Arbeidsforhold arbeidsforhold;
    private LocalDate behovForTilretteleggingFom;
    private LocalDate tilrettelagtArbeidFom;
    private Double stillingsprosent;
    private LocalDate slutteArbeidFom;
    private List<String> vedlegg;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Arbeidsforhold getArbeidsforhold() {
        return arbeidsforhold;
    }

    public void setArbeidsforhold(Arbeidsforhold arbeidsforhold) {
        this.arbeidsforhold = arbeidsforhold;
    }

    public LocalDate getBehovForTilretteleggingFom() {
        return behovForTilretteleggingFom;
    }

    public void setBehovForTilretteleggingFom(LocalDate behovForTilretteleggingFom) {
        this.behovForTilretteleggingFom = behovForTilretteleggingFom;
    }

    public LocalDate getTilrettelagtArbeidFom() {
        return tilrettelagtArbeidFom;
    }

    public void setTilrettelagtArbeidFom(LocalDate tilrettelagtArbeidFom) {
        this.tilrettelagtArbeidFom = tilrettelagtArbeidFom;
    }

    public Double getStillingsprosent() {
        return stillingsprosent;
    }

    public void setStillingsprosent(Double stillingsprosent) {
        this.stillingsprosent = stillingsprosent;
    }

    public LocalDate getSlutteArbeidFom() {
        return slutteArbeidFom;
    }

    public void setSlutteArbeidFom(LocalDate slutteArbeidFom) {
        this.slutteArbeidFom = slutteArbeidFom;
    }

    public List<String> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<String> vedlegg) {
        this.vedlegg = vedlegg;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[type=" + type + ", arbeidsforhold=" + arbeidsforhold
                + ", behovForTilretteleggingFom=" + behovForTilretteleggingFom + ", tilrettelagtArbeidFom="
                + tilrettelagtArbeidFom + ", stillingsprosent=" + stillingsprosent + ", slutteArbeidFom="
                + slutteArbeidFom + ", vedlegg=" + vedlegg + "]";
    }
}
