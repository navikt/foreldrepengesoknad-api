package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import no.nav.foreldrepenger.common.domain.Saksnummer;

@EqualsAndHashCode(callSuper = true)
public final class EngangsstønadFrontend extends SøknadFrontend {

    @Builder
    @JsonCreator
    public EngangsstønadFrontend(LocalDateTime opprettet, String type, Saksnummer saksnummer, SøkerFrontend søker, BarnFrontend barn,
                                 AnnenForelderFrontend annenForelder, UtenlandsoppholdFrontend informasjonOmUtenlandsopphold, String situasjon,
                                 Boolean erEndringssøknad, String tilleggsopplysninger, List<VedleggFrontend> vedlegg) {
        super(opprettet, type, saksnummer, søker, barn, annenForelder, informasjonOmUtenlandsopphold, situasjon,
            erEndringssøknad, tilleggsopplysninger, vedlegg);
    }

    @Override
    public String toString() {
        return "Engangsstønad{} " + super.toString();
    }
}
