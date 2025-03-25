package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static no.nav.foreldrepenger.common.util.StringUtil.mask;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;

@JsonInclude(NON_NULL)
public record BarnFrontend(@NotNull String fnr,
                           @NotNull String fornavn,
                           String mellomnavn,
                           @NotNull String etternavn,
                           @NotNull String kjønn,
                           @NotNull LocalDate fødselsdato,
                           LocalDate dødsdato,
                           AnnenForelderFrontend annenForelder) {

    @Override
    public String toString() {
        return "BarnFrontend{" +
            "fnr='" + mask(fnr) + '\'' +
            ", fornavn='" + mask(fornavn) + '\'' +
            ", mellomnavn='" + mask(mellomnavn) + '\'' +
            ", etternavn='" + mask(etternavn) + '\'' +
            ", kjønn='" + kjønn + '\'' +
            ", fødselsdato=" + fødselsdato +
            ", dødsdato=" + dødsdato +
            ", annenForelder=" + annenForelder +
            '}';
    }
}
