package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(discriminatorProperty = "type", discriminatorMapping = {
    @DiscriminatorMapping(value = "fødsel", schema = FødselDto.class),
    @DiscriminatorMapping(value = "termin", schema = TerminDto.class),
    @DiscriminatorMapping(value = "adopsjon", schema = AdopsjonDto.class),
    @DiscriminatorMapping(value = "omsorgsovertakelse", schema = OmsorgsovertakelseDto.class)})
@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = FødselDto.class, name = "fødsel"),
    @JsonSubTypes.Type(value = TerminDto.class, name = "termin"),
    @JsonSubTypes.Type(value = AdopsjonDto.class, name = "adopsjon"),
    @JsonSubTypes.Type(value = OmsorgsovertakelseDto.class, name = "omsorgsovertakelse")})
public interface BarnDto {
    int antallBarn();
}
