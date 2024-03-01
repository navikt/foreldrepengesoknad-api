package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.AssertTrue;

@JsonPropertyOrder({ "fom", "tom" })
@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UttaksPeriodeDto.class, name = "uttak"),
    @JsonSubTypes.Type(value = GradertUttaksPeriodeDto.class, name = "gradert"),
    @JsonSubTypes.Type(value = OverføringsPeriodeDto.class, name = "overføring"),
    @JsonSubTypes.Type(value = OppholdsPeriodeDto.class, name = "opphold"),
    @JsonSubTypes.Type(value = UtsettelsesPeriodeDto.class, name = "utsettelse"),
})
public interface Uttaksplanperiode {
    LocalDate fom();
    LocalDate tom();

    @AssertTrue(message = "FOM dato må være etter TOM dato!")
    default boolean isFomAfterTom() { //NOSONAR TODO
        return fom().isBefore(tom());
    }
}
