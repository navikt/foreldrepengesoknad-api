package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.time.LocalDate;

import javax.validation.Valid;

import no.nav.foreldrepenger.common.domain.AktørId;

record AnnenPartVedtakIdentifikator(@Valid AktørId annenPartAktørId,
                                    @Valid AktørId barnAktørId,
                                    LocalDate familiehendelse) {
}
