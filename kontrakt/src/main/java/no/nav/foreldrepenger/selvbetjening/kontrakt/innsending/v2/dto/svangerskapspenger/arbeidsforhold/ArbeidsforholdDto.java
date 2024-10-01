package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold;


import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = VirksomhetDto.class, name = "VIRKSOMHET"),
    @JsonSubTypes.Type(value = PrivatArbeidsgiverDto.class, name = "PRIVAT"),
    @JsonSubTypes.Type(value = SelvstendigNæringsdrivendeDto.class, name = "SELVSTENDIG"),
    @JsonSubTypes.Type(value = FrilanserDto.class, name = "FRILANSER")
})
public interface ArbeidsforholdDto {
}
