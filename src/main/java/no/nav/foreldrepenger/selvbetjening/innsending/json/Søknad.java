package no.nav.foreldrepenger.selvbetjening.innsending.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;


@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
        @Type(value = Engangsstønad.class, name = "engangsstønad"),
        @Type(value = Foreldrepengesøknad.class, name = "foreldrepenger")
})
public class Søknad {

    public String type;
    public Søker søker;
    public LocalDateTime opprettet;

    public List<Vedlegg> vedlegg;

    public Søknad(){
        vedlegg = new ArrayList<>();
    }

}
