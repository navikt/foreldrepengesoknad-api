package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class Foreldrepengesøknad extends Søknad {

    private final String dekningsgrad;
    private final List<UttaksplanPeriode> uttaksplan;

    @Builder
    @JsonCreator
    public Foreldrepengesøknad(LocalDateTime opprettet, String type, String saksnummer, Søker søker, Barn barn,
                               AnnenForelder annenForelder, Utenlandsopphold informasjonOmUtenlandsopphold,
                               String situasjon, Boolean erEndringssøknad, String tilleggsopplysninger,
                               List<Vedlegg> vedlegg, String dekningsgrad, List<UttaksplanPeriode> uttaksplan) {
        super(opprettet, type, saksnummer, søker, barn, annenForelder, informasjonOmUtenlandsopphold, situasjon,
            erEndringssøknad, tilleggsopplysninger, vedlegg);
        this.dekningsgrad = dekningsgrad;
        this.uttaksplan = uttaksplan;
    }

    public String getDekningsgrad() {
        return dekningsgrad;
    }

    public List<UttaksplanPeriode> getUttaksplan() {
        return uttaksplan;
    }

    @Override
    public String toString() {
        return "Foreldrepengesøknad{" +
            "dekningsgrad='" + dekningsgrad + '\'' +
            ", uttaksplan=" + uttaksplan +
            "} " + super.toString();
    }
}
