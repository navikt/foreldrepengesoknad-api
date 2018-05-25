package no.nav.foreldrepenger.selvbetjening.innsending.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;


// TODO remove defaultImpl when both frontend apps send json payload with type field.
@JsonTypeInfo(use = NAME, property = "type", defaultImpl = Engangsstønad.class)
@JsonSubTypes({
        @Type(value = Engangsstønad.class, name = "engangsstønad"),
        @Type(value = Foreldrepengesøknad.class, name = "foreldrepengesøknad")
})
public class Søknad {

    public String type;
    public LocalDateTime opprettet;
    public LocalDateTime sistEndret;

    public boolean type(String type) {
        if (this.type == null || type == null) {
            return false;
        }

        return this.type.equals(type);
    }

}
