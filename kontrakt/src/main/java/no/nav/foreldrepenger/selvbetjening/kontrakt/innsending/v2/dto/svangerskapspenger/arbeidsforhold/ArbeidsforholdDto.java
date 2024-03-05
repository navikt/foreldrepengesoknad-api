package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = VirksomhetDto.class, name = "virksomhet"),
    @JsonSubTypes.Type(value = PrivatArbeidsgiverDto.class, name = "privat"),
    @JsonSubTypes.Type(value = SelvstendigNæringsdrivendeDto.class, name = "selvstendig"),
    @JsonSubTypes.Type(value = FrilanserDto.class, name = "frilanser")
})
public interface ArbeidsforholdDto {
}
