package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;

@JsonInclude(NON_NULL)
public class AnnenInntekt {

    private String type;
    private String land;
    private String arbeidsgiverNavn;
    private Tidsperiode tidsperiode;
    private Boolean erNærVennEllerFamilieMedArbeisdgiver;
    private List<String> vedlegg = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getArbeidsgiverNavn() {
        return arbeidsgiverNavn;
    }

    public void setArbeidsgiverNavn(String arbeidsgiverNavn) {
        this.arbeidsgiverNavn = arbeidsgiverNavn;
    }

    public Tidsperiode getTidsperiode() {
        return tidsperiode;
    }

    public void setTidsperiode(Tidsperiode tidsperiode) {
        this.tidsperiode = tidsperiode;
    }

    public Boolean getErNærVennEllerFamilieMedArbeisdgiver() {
        return erNærVennEllerFamilieMedArbeisdgiver;
    }

    public void setErNærVennEllerFamilieMedArbeisdgiver(Boolean erNærVennEllerFamilieMedArbeisdgiver) {
        this.erNærVennEllerFamilieMedArbeisdgiver = erNærVennEllerFamilieMedArbeisdgiver;
    }

    public List<String> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<String> vedlegg) {
        this.vedlegg = vedlegg;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [type=" + getType() + ", land=" + getLand() + ", arbeidsgiverNavn="
                + getArbeidsgiverNavn() + ", tidsperiode=" + getTidsperiode()
                + ", erNærVennEllerFamilieMedArbeisdgiver="
                + getErNærVennEllerFamilieMedArbeisdgiver() + ", vedlegg=" + getVedlegg() + "]";
    }
}
