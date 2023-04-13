package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.nav.foreldrepenger.common.domain.Saksnummer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;


@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
        @Type(value = EngangsstønadFrontend.class, name = "engangsstønad"),
        @Type(value = ForeldrepengesøknadFrontend.class, name = "foreldrepenger"),
        @Type(value = SvangerskapspengesøknadFrontend.class, name = "svangerskapspenger")
})
public abstract sealed class SøknadFrontend permits EngangsstønadFrontend, ForeldrepengesøknadFrontend, SvangerskapspengesøknadFrontend {

    private LocalDateTime opprettet;
    @Pattern(regexp = BARE_BOKSTAVER)
    private final String type;
    @Valid
    private final Saksnummer saksnummer;
    @Valid
    private final SøkerFrontend søker;
    @Valid
    private final BarnFrontend barn;
    @Valid
    private final AnnenForelderFrontend annenForelder;
    @Valid
    private final UtenlandsoppholdFrontend informasjonOmUtenlandsopphold;
    @Pattern(regexp = BARE_BOKSTAVER)
    private final String situasjon;
    private final Boolean erEndringssøknad;
    @Pattern(regexp = FRITEKST)
    private final String tilleggsopplysninger;
    @VedlegglistestørrelseConstraint
    private final List<@Valid VedleggFrontend> vedlegg;

    @JsonCreator
    protected SøknadFrontend(LocalDateTime opprettet, String type, Saksnummer saksnummer, SøkerFrontend søker, BarnFrontend barn,
                             AnnenForelderFrontend annenForelder, UtenlandsoppholdFrontend informasjonOmUtenlandsopphold, String situasjon,
                             Boolean erEndringssøknad, String tilleggsopplysninger, List<VedleggFrontend> vedlegg) {
        this.opprettet = opprettet;
        this.type = type;
        this.saksnummer = saksnummer;
        this.søker = søker;
        this.barn = barn;
        this.annenForelder = annenForelder;
        this.informasjonOmUtenlandsopphold = informasjonOmUtenlandsopphold;
        this.situasjon = situasjon;
        this.erEndringssøknad = erEndringssøknad;
        this.tilleggsopplysninger = tilleggsopplysninger;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
    }

    public String getType() {
        return type;
    }

    public Saksnummer getSaksnummer() {
        return saksnummer;
    }

    public SøkerFrontend getSøker() {
        return søker;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public BarnFrontend getBarn() {
        return barn;
    }

    public AnnenForelderFrontend getAnnenForelder() {
        return annenForelder;
    }

    public UtenlandsoppholdFrontend getInformasjonOmUtenlandsopphold() {
        return informasjonOmUtenlandsopphold;
    }

    public String getSituasjon() {
        return situasjon;
    }

    public Boolean getErEndringssøknad() {
        return erEndringssøknad;
    }

    public String getTilleggsopplysninger() {
        return tilleggsopplysninger;
    }

    public List<VedleggFrontend> getVedlegg() {
        return vedlegg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SøknadFrontend that = (SøknadFrontend) o;
        return Objects.equals(opprettet, that.opprettet) && Objects.equals(type, that.type) && Objects.equals(saksnummer, that.saksnummer) && Objects.equals(søker, that.søker) && Objects.equals(barn, that.barn) && Objects.equals(annenForelder, that.annenForelder) && Objects.equals(informasjonOmUtenlandsopphold, that.informasjonOmUtenlandsopphold) && Objects.equals(situasjon, that.situasjon) && Objects.equals(erEndringssøknad, that.erEndringssøknad) && Objects.equals(tilleggsopplysninger, that.tilleggsopplysninger) && Objects.equals(vedlegg, that.vedlegg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(opprettet, type, saksnummer, søker, barn, annenForelder, informasjonOmUtenlandsopphold, situasjon, erEndringssøknad, tilleggsopplysninger, vedlegg);
    }

    @Override
    public String toString() {
        return "Søknad{" +
            "type='" + type + '\'' +
            ", saksnummer='" + saksnummer + '\'' +
            ", søker=" + søker +
            ", opprettet=" + opprettet +
            ", barn=" + barn +
            ", annenForelder=" + annenForelder +
            ", informasjonOmUtenlandsopphold=" + informasjonOmUtenlandsopphold +
            ", situasjon='" + situasjon + '\'' +
            ", erEndringssøknad=" + erEndringssøknad +
            ", tilleggsopplysninger='" + tilleggsopplysninger + '\'' +
            ", vedlegg=" + vedlegg +
            '}';
    }
}
