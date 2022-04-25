package no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "fom", "tom" })
record LukketPeriode(@NotNull LocalDate fom, @NotNull LocalDate tom){
}
