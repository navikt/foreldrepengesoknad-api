package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import no.nav.foreldrepenger.common.domain.Saksnummer;


public final class EngangsstønadFrontend extends SøknadFrontend {


    @JsonCreator
    public EngangsstønadFrontend(String type,
                                 Saksnummer saksnummer,
                                 SøkerFrontend søker,
                                 BarnFrontend barn,
                                 AnnenForelderFrontend annenForelder,
                                 UtenlandsoppholdFrontend informasjonOmUtenlandsopphold,
                                 String situasjon,
                                 Boolean erEndringssøknad,
                                 String tilleggsopplysninger,
                                 List<VedleggFrontend> vedlegg) {
        super(type, saksnummer, søker, barn, annenForelder, informasjonOmUtenlandsopphold, situasjon, erEndringssøknad,
            tilleggsopplysninger, vedlegg);
    }



    @Override
    public String toString() {
        return "Engangsstønad{} " + super.toString();
    }
}
