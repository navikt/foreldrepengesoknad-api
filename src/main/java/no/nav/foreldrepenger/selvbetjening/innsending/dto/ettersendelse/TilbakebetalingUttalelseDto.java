package no.nav.foreldrepenger.selvbetjening.innsending.dto.ettersendelse;

import no.nav.foreldrepenger.common.domain.Saksnummer;

public record TilbakebetalingUttalelseDto(YtelseType type,
                                          Saksnummer saksnummer,
                                          String dialogId,
                                          BrukerTekstDto brukerTekst) {
}
