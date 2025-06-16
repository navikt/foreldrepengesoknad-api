package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger;


import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ArbeidsforholdDto.VirksomhetDto.class, name = "virksomhet"),
    @JsonSubTypes.Type(value = ArbeidsforholdDto.PrivatArbeidsgiverDto.class, name = "privat"),
    @JsonSubTypes.Type(value = ArbeidsforholdDto.SelvstendigNæringsdrivendeDto.class, name = "selvstendig"),
    @JsonSubTypes.Type(value = ArbeidsforholdDto.FrilanserDto.class, name = "frilanser")})
public interface ArbeidsforholdDto {

    record VirksomhetDto(@Valid @NotNull Orgnummer id) implements ArbeidsforholdDto {
    }

    record PrivatArbeidsgiverDto(@Valid @NotNull Fødselsnummer id) implements ArbeidsforholdDto {
    }

    record FrilanserDto() implements ArbeidsforholdDto {
    }

    record SelvstendigNæringsdrivendeDto() implements ArbeidsforholdDto {
    }
}
