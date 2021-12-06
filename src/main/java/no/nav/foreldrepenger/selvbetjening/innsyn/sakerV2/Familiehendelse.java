package no.nav.foreldrepenger.selvbetjening.innsyn.sakerV2;

import java.time.LocalDate;

record Familiehendelse(LocalDate fødselsdato,
                       LocalDate termindato,
                       int antallBarn,
                       LocalDate omsorgsovertakelse) {
}
