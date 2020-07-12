package no.nav.foreldrepenger.selvbetjening.innsending.pdf;

import lombok.Data;

@Data
public class TilbakebetalingUttalelseDto {
    private final String navn;
    private final String fnr;
    private final String saksnummer;
    private final String ytelse;
    private final String innsendtDato;
    private final String tilsvar;
}
