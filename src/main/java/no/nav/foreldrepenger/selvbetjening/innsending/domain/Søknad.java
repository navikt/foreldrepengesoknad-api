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
        @Type(value = Engangsstønad.class, name = "engangsstønad"),
        @Type(value = Foreldrepengesøknad.class, name = "foreldrepenger"),
        @Type(value = Svangerskapspengesøknad.class, name = "svangerskapspenger")
})
public abstract sealed class Søknad permits Engangsstønad,Foreldrepengesøknad,Svangerskapspengesøknad {

    private LocalDateTime opprettet;
    private final String type;
    private final String saksnummer;
    private final Søker søker;
    private final Barn barn;
    private final AnnenForelder annenForelder;
    private final Utenlandsopphold informasjonOmUtenlandsopphold;
    private final String situasjon;
    private final Boolean erEndringssøknad;
    private final String tilleggsopplysninger;
    private final List<Vedlegg> vedlegg;

    @JsonCreator
    protected Søknad(LocalDateTime opprettet, String type, String saksnummer, Søker søker, Barn barn,
                  AnnenForelder annenForelder, Utenlandsopphold informasjonOmUtenlandsopphold, String situasjon,
                  Boolean erEndringssøknad, String tilleggsopplysninger, List<Vedlegg> vedlegg) {
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

    public Søker getSøker() {
        return søker;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public Barn getBarn() {
        return barn;
    }

    public AnnenForelder getAnnenForelder() {
        return annenForelder;
    }

    public Utenlandsopphold getInformasjonOmUtenlandsopphold() {
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

    public List<Vedlegg> getVedlegg() {
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
