package no.nav.foreldrepenger.selvbetjening.innsending.domain.tilbakebetaling;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.BrukerTekst;

public record TilbakebetalingUttalelse(String type,
                                       Saksnummer saksnummer,
                                       String dialogId,
                                       BrukerTekst brukerTekst) {
}
