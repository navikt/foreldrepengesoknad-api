package no.nav.foreldrepenger.selvbetjening.innsyn.saker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Navn {
    private final String fornavn;
    private final String mellomnavn;
    private final String etternavn;
    private final Kjønn kjønn;

    @JsonCreator
    public Navn(@JsonProperty("fornavn") String fornavn, @JsonProperty("mellomnavn") String mellomnavn,
            @JsonProperty("etternavn") String etternavn, @JsonProperty("kjønn") Kjønn kjønn) {
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.kjønn = kjønn;
    }

    public Kjønn getKjønn() {
        return kjønn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getMellomnavn() {
        return mellomnavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fornavn=" + fornavn + ", mellomnavn=" + mellomnavn + ", etternavn=" + etternavn + ", kjønn=" + kjønn
                + "]";
    }

}
