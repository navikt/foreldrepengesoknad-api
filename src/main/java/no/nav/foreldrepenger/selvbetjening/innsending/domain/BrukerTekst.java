package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BrukerTekst {

    @NotNull
    private final String dokumentType;
    private final String tekst;
    private final String overskrift;

    @JsonCreator
    public BrukerTekst(@JsonProperty("dokumentType") String dokumentType, @JsonProperty("tekst") String tekst,
            @JsonProperty("overskrift") String overskrift) {
        this.dokumentType = dokumentType;
        this.tekst = tekst;
        this.overskrift = overskrift;
    }

    public String getDokumentType() {
        return dokumentType;
    }

    public String getTekst() {
        return tekst;
    }

    public String getOverskrift() {
        return overskrift;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dokumentType=" + dokumentType + ", tekst=" + tekst + ", overskrift="
                + overskrift + "]";
    }
}
