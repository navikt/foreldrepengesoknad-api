package no.nav.foreldrepenger.selvbetjening.innsending.domain.tilbakebetaling;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.BrukerTekst;

public record TilbakebetalingUttalelse(
        String type,
        String saksnummer,
        String dialogId,
        BrukerTekst brukerTekst) {
}
