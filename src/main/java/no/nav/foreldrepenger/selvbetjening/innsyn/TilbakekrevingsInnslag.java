package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.time.LocalDate;

import no.nav.foreldrepenger.common.domain.Saksnummer;

public record TilbakekrevingsInnslag(Saksnummer saksnummer, LocalDate opprettet) {
}
