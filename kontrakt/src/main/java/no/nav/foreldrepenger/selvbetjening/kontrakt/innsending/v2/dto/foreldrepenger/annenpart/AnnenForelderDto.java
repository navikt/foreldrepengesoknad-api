package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.foreldrepenger.annenpart;


import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = NorskForelderDto.class, name = "norsk"),
    @JsonSubTypes.Type(value = UtenlandskForelderDto.class, name = "utenlandsk")
})
public interface AnnenForelderDto {
    Fødselsnummer fnr();
    Rettigheter rettigheter();

    record Rettigheter(@NotNull Boolean harRettPåForeldrepenger,
                       @NotNull Boolean erInformertOmSøknaden,
                       Boolean erAleneOmOmsorg,
                       Boolean harMorUføretrygd,
                       Boolean harAnnenForelderOppholdtSegIEØS,
                       Boolean harAnnenForelderTilsvarendeRettEØS) {
    }
}
