package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;

public record TilbakebetalingUttalelseDto(String navn,
                                          Fødselsnummer fnr,
                                          Saksnummer saksnummer,
                                          String ytelse,
                                          String innsendtDato,
                                          String tilsvar) {
}
