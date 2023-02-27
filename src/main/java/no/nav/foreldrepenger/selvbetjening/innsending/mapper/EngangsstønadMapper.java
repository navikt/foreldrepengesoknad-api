package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilMedlemskap;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilRelasjonTilBarn;

import java.time.LocalDate;
import java.util.ArrayList;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.Ytelse;
import no.nav.foreldrepenger.common.domain.engangsstønad.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EngangsstønadFrontend;

final class EngangsstønadMapper {

    private EngangsstønadMapper() {
    }

    static no.nav.foreldrepenger.common.domain.Søknad tilEngangsstønad(EngangsstønadFrontend e) {
        return new Søknad(
            LocalDate.now(),
            tilSøker(e),
            tilYtelse(e),
            e.getTilleggsopplysninger(),
            new ArrayList<>() // Settes av InnsendingConnection etter logging
        );
    }

    private static Søker tilSøker(EngangsstønadFrontend e) {
        var søker = e.getSøker();
        if (søker == null) {
            throw new IllegalStateException("Kan ikke ha tom søkerobjekt");
        }
        return new Søker(BrukerRolle.MOR, søker.språkkode());
    }

    private static Ytelse tilYtelse(EngangsstønadFrontend e) {
        return new Engangsstønad(
            tilMedlemskap(e),
            tilRelasjonTilBarn(e)
        );
    }
}
