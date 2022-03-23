package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.persondetaljer;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.Objects;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.PersonDetaljer;

public record AktørId(@Pattern(regexp = FRITEKST) String value) implements PersonDetaljer {

    @JsonCreator
    public AktørId {
        Objects.requireNonNull(value, "AktørId kan ikke være null");
    }

    @JsonProperty("aktørId")
    public String value() {
        return value;
    }

}
