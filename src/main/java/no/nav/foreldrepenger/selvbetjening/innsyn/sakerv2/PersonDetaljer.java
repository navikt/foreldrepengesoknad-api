package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.persondetaljer.AktørId;
import no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.persondetaljer.Person;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AktørId.class, name = "aktørId"),
    @JsonSubTypes.Type(value = Person.class, name = "person")
})
public interface PersonDetaljer {
}
