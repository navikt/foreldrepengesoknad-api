package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
        @Type(value = Engangsstønad.class, name = "engangsstønad"),
        @Type(value = Foreldrepengesøknad.class, name = "foreldrepenger")
})
public class Søknad {

    public String type;
    public String saksnummer;
    public Søker søker;
    public LocalDateTime opprettet;

    public Barn barn;
    public AnnenForelder annenForelder;
    public Utenlandsopphold informasjonOmUtenlandsopphold;

    public String situasjon;
    public Boolean erEndringssøknad;
    public String tilleggsopplysninger;

    public List<Vedlegg> vedlegg;

    public Søknad() {
        vedlegg = new ArrayList<>();
    }
}
