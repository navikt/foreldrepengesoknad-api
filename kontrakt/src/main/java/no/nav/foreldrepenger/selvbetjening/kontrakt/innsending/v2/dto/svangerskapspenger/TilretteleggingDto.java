package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HelTilretteleggingDto.class, name = "hel"),
    @JsonSubTypes.Type(value = DelvisTilretteleggingDto.class, name = "delvis"),
    @JsonSubTypes.Type(value = IngenTilretteleggingDto.class, name = "ingen")
})
public interface TilretteleggingDto {

    ArbeidsforholdDto arbeidsforhold();
    LocalDate behovForTilretteleggingFom();
    List<MutableVedleggReferanseDto> vedleggsreferanser();
}
