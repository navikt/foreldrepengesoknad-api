package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public record VedleggDto(String type, VedleggMetadataDto metadata, byte[] vedlegg) {

}
