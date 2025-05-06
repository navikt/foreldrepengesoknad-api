package no.nav.foreldrepenger.selvbetjening.oppslag.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static no.nav.foreldrepenger.common.util.StringUtil.mask;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;

@JsonInclude(NON_NULL)
public record AnnenForelderFrontend(@NotNull String fnr,
                                    @NotNull String fornavn,
                                    String mellomnavn,
                                    @NotNull String etternavn,
                                    LocalDate fødselsdato) {

    @Override
    public String toString() {
        return "AnnenForelder{" +
            "fnr='" + mask(fnr) + '\'' +
            ", fornavn='" + mask(fornavn) + '\'' +
            ", mellomnavn='" + mask(mellomnavn) + '\'' +
            ", etternavn='" + mask(etternavn) + '\'' +
            ", fødselsdato=" + fødselsdato +
            '}';
    }
}
