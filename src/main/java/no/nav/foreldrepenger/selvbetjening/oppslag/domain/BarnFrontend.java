package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

@JsonInclude(NON_NULL)
public record BarnFrontend(String fornavn,
                           String mellomnavn,
                           String etternavn,
                           String kjønn,
                           String fnr,
                           LocalDate fødselsdato,
                           AnnenForelderFrontend annenForelder) {

    @Override
    public String toString() {
        return "Barn{" +
            "fornavn='" + fornavn + '\'' +
            ", mellomnavn='" + mellomnavn + '\'' +
            ", etternavn='" + etternavn + '\'' +
            ", kjønn=" + kjønn +
            ", fnr='" + StringUtil.maskFnr(fnr) + '\'' +
            ", fødselsdato=" + fødselsdato +
            ", annenForelder=" + annenForelder +
            '}';
    }
}
