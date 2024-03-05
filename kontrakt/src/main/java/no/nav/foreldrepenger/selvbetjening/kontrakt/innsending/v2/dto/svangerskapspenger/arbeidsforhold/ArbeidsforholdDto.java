package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold;


import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = VirksomhetDto.class, names = {"virksomhet", "VIRKSOMHET"}),
    @JsonSubTypes.Type(value = PrivatArbeidsgiverDto.class, names = {"privat", "PRIVAT"}),
    @JsonSubTypes.Type(value = SelvstendigNæringsdrivendeDto.class, names = {"selvstendig", "SELVSTENDIG"}),
    @JsonSubTypes.Type(value = FrilanserDto.class, names = {"frilanser", "FRILANSER"})
})
public interface ArbeidsforholdDto {
}
