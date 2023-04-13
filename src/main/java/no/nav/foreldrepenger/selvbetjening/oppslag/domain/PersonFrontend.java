package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static no.nav.foreldrepenger.common.util.StringUtil.mask;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;

@JsonInclude(NON_EMPTY)
public record PersonFrontend(Fødselsnummer fnr,
                             String fornavn,
                             String mellomnavn,
                             String etternavn,
                             String kjønn,
                             LocalDate fødselsdato,
                             boolean ikkeNordiskEøsLand, // Ikke i bruk i frontend!
                             Bankkonto bankkonto,
                             List<BarnFrontend> barn,
                             Sivilstand sivilstand) {

    @Override
    public String toString() {
        return "Person{" +
            "fnr='" + fnr + '\'' +
            ", fornavn='" + mask(fornavn) + '\'' +
            ", mellomnavn='" + mask(mellomnavn) + '\'' +
            ", etternavn='" + mask(etternavn) + '\'' +
            ", kjønn='" + kjønn + '\'' +
            ", fødselsdato=" + fødselsdato +
            ", ikkeNordiskEøsLand=" + ikkeNordiskEøsLand +
            ", bankkonto=" + bankkonto +
            ", barn=" + barn +
            ", sivilstand=" + sivilstand +
            '}';
    }
}
