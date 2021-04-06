package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.time.LocalDate;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Foreldrepengesøknad;

public record RettigheterDto(
        Boolean harAnnenForelderRett,
        Boolean harAleneOmsorgForBarnet,
        LocalDate datoForAleneomsorg) {

    public RettigheterDto(Foreldrepengesøknad foreldrepengesøknad) {
        this(foreldrepengesøknad.getSøker().erAleneOmOmsorg(), foreldrepengesøknad.getAnnenForelder().getHarRettPåForeldrepenger(),
                foreldrepengesøknad.getAnnenForelder().getDatoForAleneomsorg());
    }
}