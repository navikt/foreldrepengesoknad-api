package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.time.LocalDate;

import javax.validation.Valid;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;

record AnnenPartVedtakIdentifikator(@Valid Fødselsnummer annenPartFødselsnummer,
                                    @Valid Fødselsnummer barnFødselsnummer,
                                    LocalDate familiehendelse) {
}
