package no.nav.foreldrepenger.selvbetjening.nedetid;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import jakarta.validation.constraints.Pattern;

public record NedetidInfo(@Pattern(regexp = FRITEKST) String fra,
                          @Pattern(regexp = FRITEKST) String til,
                          @Pattern(regexp = FRITEKST) String msg) {

}
