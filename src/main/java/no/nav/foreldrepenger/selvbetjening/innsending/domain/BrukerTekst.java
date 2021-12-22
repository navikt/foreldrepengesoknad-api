package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import javax.validation.constraints.NotNull;

public record BrukerTekst(@NotNull String dokumentType,
                          String tekst,
                          String overskrift) {
}
