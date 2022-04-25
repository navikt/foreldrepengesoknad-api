package no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan;

import java.time.LocalDate;

record SøknadsGrunnlag(LocalDate termindato,
                       LocalDate fødselsdato,
                       LocalDate omsorgsovertakelsesdato,
                       Integer dekningsgrad,
                       Integer antallBarn,
                       Boolean søkerErFarEllerMedmor,
                       Boolean morErAleneOmOmsorg,
                       Boolean morHarRett,
                       Boolean morErUfør,
                       Boolean farMedmorErAleneOmOmsorg,
                       Boolean farMedmorHarRett,
                       Boolean annenForelderErInformert) {
}
