package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.uttaksplan;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(discriminatorProperty = "type", discriminatorMapping = {
    @DiscriminatorMapping(schema = UttaksPeriodeDto.class, value = "uttak"),
    @DiscriminatorMapping(schema = OverføringsPeriodeDto.class, value = "overføring"),
    @DiscriminatorMapping(schema = OppholdsPeriodeDto.class, value = "opphold"),
    @DiscriminatorMapping(schema = UtsettelsesPeriodeDto.class, value = "utsettelse")})
@JsonPropertyOrder({"fom", "tom"})
@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UttaksPeriodeDto.class, name = "uttak"),
    @JsonSubTypes.Type(value = OverføringsPeriodeDto.class, name = "overføring"),
    @JsonSubTypes.Type(value = OppholdsPeriodeDto.class, name = "opphold"),
    @JsonSubTypes.Type(value = UtsettelsesPeriodeDto.class, name = "utsettelse")})
public interface Uttaksplanperiode {
    LocalDate fom();

    LocalDate tom();
}
