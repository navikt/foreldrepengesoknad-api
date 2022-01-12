package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static no.nav.foreldrepenger.common.util.StringUtil.mask;
import static no.nav.foreldrepenger.selvbetjening.util.StringUtil.maskFnr;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public record AnnenForelderFrontend(String fnr,
                                    String fornavn,
                                    String mellomnavn,
                                    String etternavn,
                                    LocalDate fødselsdato) {

    @Override
    public String toString() {
        return "AnnenForelder{" +
            "fnr='" + maskFnr(fnr) + '\'' +
            ", fornavn='" + fornavn + '\'' +
            ", mellomnavn='" + mask(mellomnavn) + '\'' +
            ", etternavn='" + mask(etternavn) + '\'' +
            ", fødselsdato=" + fødselsdato +
            '}';
    }
}
