package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilMedlemskap;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilRelasjonTilBarn;

import java.util.ArrayList;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Ytelse;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EngangsstønadFrontend;

final class EngangsstønadMapper {

    private EngangsstønadMapper() {
    }

    static no.nav.foreldrepenger.common.domain.Søknad tilEngangsstønad(EngangsstønadFrontend e) {
        return no.nav.foreldrepenger.common.domain.Søknad.builder()
            .søker(tilSøker(e))
            .ytelse(tilYtelse(e))
            .vedlegg(new ArrayList<>()) // Settes av InnsendingConnection etter logging
            .build();
    }

    private static Søker tilSøker(EngangsstønadFrontend e) {
        var søker = e.getSøker();
        if (søker == null) {
            throw new IllegalStateException("Kan ikke ha tom søkerobjekt");
        }
        return new Søker(BrukerRolle.MOR, søker.språkkode());
    }

    private static Ytelse tilYtelse(EngangsstønadFrontend e) {
        var engangsstønadBuilder = no.nav.foreldrepenger.common.domain.engangsstønad.Engangsstønad.builder();
        if (Boolean.FALSE.equals(e.getErEndringssøknad())) {
            engangsstønadBuilder.medlemsskap(tilMedlemskap(e));
        }
        return engangsstønadBuilder
            // .annenForelder() // Sendes ikke ned fra frontend
            .relasjonTilBarn(tilRelasjonTilBarn(e))
            .build();
    }
}
