package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Arbeidsforhold {

    private final String arbeidsgiverId;
    private final String arbeidsgiverIdType;
    private final String arbeidsgiverNavn;
    private final Double stillingsprosent;
    private final LocalDate fom;
    private final LocalDate tom;

    @JsonCreator
    public Arbeidsforhold(@JsonProperty("arbeidsgiverId") String arbeidsgiverId,
            @JsonProperty("arbeidsgiverIdType") String arbeidsgiverIdType,
            @JsonProperty("arbeidsgiverNavn") String arbeidsgiverNavn,
            @JsonProperty("stillingsprosent") Double stillingsprosent,
            @JsonProperty("fom") @JsonAlias("from") LocalDate fom,
            @JsonProperty("tom") @JsonAlias("to") LocalDate tom) {
        this.arbeidsgiverId = arbeidsgiverId;
        this.arbeidsgiverIdType = arbeidsgiverIdType;
        this.arbeidsgiverNavn = arbeidsgiverNavn;
        this.stillingsprosent = stillingsprosent;
        this.fom = fom;
        this.tom = tom;
    }
}
