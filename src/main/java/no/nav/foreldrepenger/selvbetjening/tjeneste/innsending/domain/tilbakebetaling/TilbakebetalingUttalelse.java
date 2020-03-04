package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.tilbakebetaling;

import lombok.Data;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.BrukerTekst;

@Data
public class TilbakebetalingUttalelse {
    private final String type;
    private final String saksnummer;
    private final String dialogId;
    private final BrukerTekst brukerTekst;
}
