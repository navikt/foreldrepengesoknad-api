package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static java.util.Collections.emptyList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
        @Type(value = EngangsstønadFrontend.class, name = "engangsstønad"),
        @Type(value = ForeldrepengesøknadFrontend.class, name = "foreldrepenger"),
        @Type(value = SvangerskapspengesøknadFrontend.class, name = "svangerskapspenger")
})
public abstract sealed class SøknadFrontend permits EngangsstønadFrontend, ForeldrepengesøknadFrontend, SvangerskapspengesøknadFrontend {

    private LocalDateTime opprettet;
    private final String type;
    private final String saksnummer;
    private final SøkerFrontend søker;
    private final BarnFrontend barn;
    private final AnnenForelderFrontend annenForelder;
    private final UtenlandsoppholdFrontend informasjonOmUtenlandsopphold;
    private final String situasjon;
    private final Boolean erEndringssøknad;
    private final String tilleggsopplysninger;
    private final List<VedleggFrontend> vedlegg;

    @JsonCreator
    protected SøknadFrontend(LocalDateTime opprettet, String type, String saksnummer, SøkerFrontend søker, BarnFrontend barn,
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

    public String getSaksnummer() {
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
