package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;

public record TilbakebetalingUttalelseDto(String navn,
                                          Fødselsnummer fnr,
                                          String saksnummer,
                                          String ytelse,
                                          String innsendtDato,
                                          String tilsvar) {
}
