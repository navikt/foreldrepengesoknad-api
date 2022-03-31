package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_TALL;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class ForeldrepengesøknadFrontend extends SøknadFrontend {

    @Pattern(regexp = BARE_TALL)
    private final String dekningsgrad;
    @Valid
    private final List<UttaksplanPeriode> uttaksplan;

    @Builder
    @JsonCreator
    public ForeldrepengesøknadFrontend(LocalDateTime opprettet, String type, String saksnummer, SøkerFrontend søker, BarnFrontend barn,
                                       AnnenForelderFrontend annenForelder, UtenlandsoppholdFrontend informasjonOmUtenlandsopphold,
                                       String situasjon, Boolean erEndringssøknad, String tilleggsopplysninger,
                                       List<VedleggFrontend> vedlegg, String dekningsgrad, List<UttaksplanPeriode> uttaksplan) {
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
