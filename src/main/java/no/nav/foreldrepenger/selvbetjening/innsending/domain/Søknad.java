package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.EqualsAndHashCode;

@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
        @Type(value = Engangsstønad.class, name = "engangsstønad"),
        @Type(value = Foreldrepengesøknad.class, name = "foreldrepenger"),
        @Type(value = Svangerskapspengesøknad.class, name = "svangerskapspenger")
})
@EqualsAndHashCode
public class Søknad {

    private String type;
    private String saksnummer;
    private Søker søker;
    private LocalDateTime opprettet;

    private Barn barn;
    private AnnenForelder annenForelder;
    private Utenlandsopphold informasjonOmUtenlandsopphold;

    private String situasjon;
    private Boolean erEndringssøknad;
    private String tilleggsopplysninger;

    private List<Vedlegg> vedlegg;

    public Søknad() {
        setVedlegg(new ArrayList<>());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    public void setSaksnummer(String saksnummer) {
        this.saksnummer = saksnummer;
    }

    public Søker getSøker() {
        return søker;
    }

    public void setSøker(Søker søker) {
        this.søker = søker;
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

    public void setBarn(Barn barn) {
        this.barn = barn;
    }

    public AnnenForelder getAnnenForelder() {
        return annenForelder;
    }

    public void setAnnenForelder(AnnenForelder annenForelder) {
        this.annenForelder = annenForelder;
    }

    public Utenlandsopphold getInformasjonOmUtenlandsopphold() {
        return informasjonOmUtenlandsopphold;
    }

    public void setInformasjonOmUtenlandsopphold(Utenlandsopphold informasjonOmUtenlandsopphold) {
        this.informasjonOmUtenlandsopphold = informasjonOmUtenlandsopphold;
    }

    public String getSituasjon() {
        return situasjon;
    }

    public void setSituasjon(String situasjon) {
        this.situasjon = situasjon;
    }

    public Boolean getErEndringssøknad() {
        return erEndringssøknad;
    }

    public void setErEndringssøknad(Boolean erEndringssøknad) {
        this.erEndringssøknad = erEndringssøknad;
    }

    public String getTilleggsopplysninger() {
        return tilleggsopplysninger;
    }

    public void setTilleggsopplysninger(String tilleggsopplysninger) {
        this.tilleggsopplysninger = tilleggsopplysninger;
    }

    public List<Vedlegg> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<Vedlegg> vedlegg) {
        this.vedlegg = vedlegg;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[type=" + type + ", saksnummer=" + saksnummer + ", søker=" + søker
                + ", opprettet=" + opprettet + ", barn=" + barn + ", annenForelder=" + annenForelder
                + ", informasjonOmUtenlandsopphold=" + informasjonOmUtenlandsopphold + ", situasjon=" + situasjon
                + ", erEndringssøknad=" + erEndringssøknad + ", tilleggsopplysninger=" + tilleggsopplysninger
                + ", vedlegg=" + vedlegg + "]";
    }
}
