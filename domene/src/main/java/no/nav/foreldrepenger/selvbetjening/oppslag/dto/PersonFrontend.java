package no.nav.foreldrepenger.selvbetjening.oppslag.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static no.nav.foreldrepenger.common.util.StringUtil.mask;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.felles.Kjønn;

@JsonInclude(NON_NULL)
public record PersonFrontend(@NotNull Fødselsnummer fnr,
                             @NotNull String fornavn,
                             String mellomnavn,
                             @NotNull String etternavn,
                             @NotNull Kjønn kjønn,
                             @NotNull LocalDate fødselsdato,
                             Bankkonto bankkonto,
                             @NotNull List<BarnFrontend> barn,
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
            ", bankkonto=" + bankkonto +
            ", barn=" + barn +
            ", sivilstand=" + sivilstand +
            '}';
    }
}
