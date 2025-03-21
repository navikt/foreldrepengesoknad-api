package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.InnsendingsType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.ArbeidsforholdDto;

import java.util.List;
import java.util.UUID;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

public record VedleggDto(UUID uuid,
                         @NotNull DokumentType skjemanummer,
                         InnsendingsType innsendingsType,
                         @Pattern(regexp = FRITEKST) String beskrivelse,
                         @Valid Dokumenterer dokumenterer,
                         @JsonIgnore VedleggReferanse referanse) {
    public VedleggDto {
        referanse = VedleggReferanse.fra(uuid);
    }

    @JsonCreator
    public VedleggDto(UUID uuid, DokumentType skjemanummer, InnsendingsType innsendingsType, String beskrivelse, Dokumenterer dokumenterer) {
        this(uuid, skjemanummer, innsendingsType, beskrivelse, dokumenterer, null);
    }

    public record Dokumenterer(@NotNull Type type,
                               @Valid ArbeidsforholdDto arbeidsforhold,
                               @Valid @Size(max = 100) List<@Valid @NotNull ÅpenPeriodeDto> perioder) {
        public enum Type {
            BARN,
            OPPTJENING,
            UTTAK,
            TILRETTELEGGING,
        }
    }
}
