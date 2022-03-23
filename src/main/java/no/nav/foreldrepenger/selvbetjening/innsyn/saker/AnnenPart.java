package no.nav.foreldrepenger.selvbetjening.innsyn.saker;

import javax.validation.Valid;

import no.nav.foreldrepenger.common.domain.AktørId;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;

public record AnnenPart(@Valid Fødselsnummer fnr,
                        @Valid AktørId aktørId,
                        @Valid Navn navn) {
    @Override
    public String toString() {
        return "AnnenPart{" +
            "fnr='" + fnr + '\'' +
            ", aktørId=" + aktørId +
            ", navn=" + navn +
            '}';
    }
}
