package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Situasjon;

public record BarnHelper(BarnDto barn, Situasjon situasjon) {
}
