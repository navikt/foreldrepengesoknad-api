package no.nav.foreldrepenger.selvbetjening.innsyn.saker;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;
import static no.nav.foreldrepenger.common.util.StringUtil.mask;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"fornavn", "mellomnavn", "etternavn", "kjønn"})
public record Navn(@Pattern(regexp = FRITEKST) String fornavn,
                   @Pattern(regexp = FRITEKST) String mellomnavn,
                   @Pattern(regexp = FRITEKST) String etternavn,
                   Kjønn kjønn) {

    @Override
    public String toString() {
        return "Navn{" +
            "fornavn='" + mask(fornavn) + '\'' +
            ", mellomnavn='" +  mask(mellomnavn) + '\'' +
            ", etternavn='" +  mask(etternavn) + '\'' +
            ", kjønn=" + kjønn +
            '}';
    }
}
