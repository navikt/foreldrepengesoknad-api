package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static no.nav.foreldrepenger.common.util.StringUtil.mask;
import static no.nav.foreldrepenger.selvbetjening.util.StringUtil.maskFnr;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

// TODO: Konsolider modell her med Person i sakerV2 og/eller Person fra mottak/felles
@JsonInclude(NON_EMPTY)
public record PersonFrontend(String fnr,
                             String fornavn,
                             String mellomnavn,
                             String etternavn,
                             String kjønn,
                             LocalDate fødselsdato,
                             boolean ikkeNordiskEøsLand, // Ikke i bruk i frontend!
                             List<BarnFrontend> barn) {

    @Override
    public String toString() {
        return "Person{" +
            "fnr='" + maskFnr(fnr) + '\'' +
            ", fornavn='" + fornavn + '\'' +
            ", mellomnavn='" + mask(mellomnavn) + '\'' +
            ", etternavn='" + mask(etternavn) + '\'' +
            ", kjønn='" + kjønn + '\'' +
            ", fødselsdato=" + fødselsdato +
            ", ikkeNordiskEøsLand=" + ikkeNordiskEøsLand +
            ", barn=" + barn +
            '}';
    }
}
