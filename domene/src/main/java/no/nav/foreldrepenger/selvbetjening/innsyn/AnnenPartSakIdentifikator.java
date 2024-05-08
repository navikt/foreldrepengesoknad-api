package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.time.LocalDate;

import jakarta.validation.Valid;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;

record AnnenPartSakIdentifikator(@Valid Fødselsnummer annenPartFødselsnummer,
                                 @Valid Fødselsnummer barnFødselsnummer,
                                 LocalDate familiehendelse) {
}
