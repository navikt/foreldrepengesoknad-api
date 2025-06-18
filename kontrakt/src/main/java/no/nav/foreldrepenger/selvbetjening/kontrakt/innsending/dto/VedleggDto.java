package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;

public record VedleggDto(UUID uuid,
                         @NotNull DokumentType skjemanummer,
                         VedleggInnsendingType innsendingsType,
                         @Pattern(regexp = FRITEKST) String beskrivelse,
                         @Valid Dokumenterer dokumenterer,
                         @JsonIgnore VedleggReferanse referanse) {

    public VedleggDto {
        referanse = VedleggReferanse.fra(uuid);
    }

    @JsonCreator
    public VedleggDto(UUID uuid, DokumentType skjemanummer, VedleggInnsendingType innsendingsType, String beskrivelse, Dokumenterer dokumenterer) {
        this(uuid, skjemanummer, innsendingsType, beskrivelse, dokumenterer, null);
    }

    public boolean erOpplastetVedlegg() {
        return innsendingsType == null || innsendingsType.equals(VedleggInnsendingType.LASTET_OPP);
    }

    public record Dokumenterer(@NotNull VedleggDto.Dokumenterer.DokumentererType type,
                               @Valid ArbeidsforholdDto arbeidsforhold,
                               @Valid @Size(max = 200) List<@Valid @NotNull ÅpenPeriodeDto> perioder) {
        public enum DokumentererType {
            BARN,
            OPPTJENING,
            UTTAK,
            TILRETTELEGGING,
        }
    }
}
