package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

@JsonInclude(NON_NULL)
public record AnnenForelderFrontend(String fnr,
                                    String fornavn,
                                    String mellomnavn,
                                    String etternavn,
                                    LocalDate fødselsdato) {

    @Override
    public String toString() {
        return "AnnenForelder{" +
            "fnr='" + StringUtil.maskFnr(fnr) + '\'' +
            ", fornavn='" + fornavn + '\'' +
            ", mellomnavn='" + mellomnavn + '\'' +
            ", etternavn='" + etternavn + '\'' +
            ", fødselsdato=" + fødselsdato +
            '}';
    }
}
