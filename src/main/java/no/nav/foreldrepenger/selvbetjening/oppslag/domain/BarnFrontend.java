package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static no.nav.foreldrepenger.common.util.StringUtil.mask;
import static no.nav.foreldrepenger.common.util.StringUtil.partialMask;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public record BarnFrontend(String fnr,
                           String fornavn,
                           String mellomnavn,
                           String etternavn,
                           String kjønn,
                           LocalDate fødselsdato,
                           AnnenForelderFrontend annenForelder) {

    @Override
    public String toString() {
        return "BarnFrontend{" +
            "fnr='" + partialMask(fnr) + '\'' +
            ", fornavn='" + fornavn + '\'' +
            ", mellomnavn='" + mask(mellomnavn) + '\'' +
            ", etternavn='" + mask(etternavn) + '\'' +
            ", kjønn='" + kjønn + '\'' +
            ", fødselsdato=" + fødselsdato +
            ", annenForelder=" + annenForelder +
            '}';
    }
}
