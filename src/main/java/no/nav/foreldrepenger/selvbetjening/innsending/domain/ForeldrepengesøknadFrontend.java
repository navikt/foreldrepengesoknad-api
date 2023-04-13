package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import no.nav.foreldrepenger.common.domain.Saksnummer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_TALL;


public final class ForeldrepengesøknadFrontend extends SøknadFrontend {

    @NotNull
    @Pattern(regexp = BARE_TALL)
    private final String dekningsgrad;
    @Valid
    private final List<UttaksplanPeriode> uttaksplan;

    private final Boolean ønskerJustertUttakVedFødsel;

    @JsonCreator
    public ForeldrepengesøknadFrontend(LocalDateTime opprettet,
                                       String type,
                                       Saksnummer saksnummer,
                                       SøkerFrontend søker,
                                       BarnFrontend barn,
                                       AnnenForelderFrontend annenForelder,
                                       UtenlandsoppholdFrontend informasjonOmUtenlandsopphold,
                                       String situasjon,
                                       Boolean erEndringssøknad,
                                       String tilleggsopplysninger,
                                       List<VedleggFrontend> vedlegg,
                                       String dekningsgrad,
                                       List<UttaksplanPeriode> uttaksplan,
                                       Boolean ønskerJustertUttakVedFødsel) {
        super(opprettet, type, saksnummer, søker, barn, annenForelder, informasjonOmUtenlandsopphold, situasjon,
            erEndringssøknad, tilleggsopplysninger, vedlegg);
        this.dekningsgrad = dekningsgrad;
        this.uttaksplan = uttaksplan;
        this.ønskerJustertUttakVedFødsel = ønskerJustertUttakVedFødsel;
    }

    public String getDekningsgrad() {
        return dekningsgrad;
    }

    public List<UttaksplanPeriode> getUttaksplan() {
        return uttaksplan;
    }

    public Boolean isØnskerJustertUttakVedFødsel() {
        return ønskerJustertUttakVedFødsel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ForeldrepengesøknadFrontend that = (ForeldrepengesøknadFrontend) o;
        return Objects.equals(dekningsgrad, that.dekningsgrad) && Objects.equals(uttaksplan, that.uttaksplan) && Objects.equals(ønskerJustertUttakVedFødsel, that.ønskerJustertUttakVedFødsel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dekningsgrad, uttaksplan, ønskerJustertUttakVedFødsel);
    }

    @Override
    public String toString() {
        return "Foreldrepengesøknad{" +
            "dekningsgrad='" + dekningsgrad + '\'' +
            ", uttaksplan=" + uttaksplan +
            ", ønskerJustertUttakVedFødsel=" + ønskerJustertUttakVedFødsel +
            "} " + super.toString();
    }
}
