package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.felles.InnsendingsType.LASTET_OPP;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;

public class VedleggDto {

    private byte[] content;

    @Pattern(regexp = FRITEKST)
    private final String uuid;
    @Pattern(regexp = FRITEKST)
    private final String filename;
    @Pattern(regexp = FRITEKST)
    private final String beskrivelse;
    @Pattern(regexp = "^[\\p{Digit}\\p{L}_]*$")
    private final String innsendingsType;
    @Pattern(regexp = "^[\\p{Digit}\\p{L}]*$")
    private final String skjemanummer;
    @Valid
    private final Dokumenterer dokumenterer;

    public VedleggDto(byte[] content, String beskrivelse, String skjemanummer) {
        this(content, beskrivelse, LASTET_OPP.name(), skjemanummer, UUID.randomUUID().toString(), null, null);
    }

    @JsonCreator
    public VedleggDto(byte[] content, String beskrivelse, String innsendingsType, String skjemanummer, String uuid, String filename, Dokumenterer dokumenterer) {
        this.content = content;
        this.beskrivelse = beskrivelse;
        this.innsendingsType = innsendingsType;
        this.skjemanummer = skjemanummer;
        this.uuid = uuid;
        this.filename = filename;
        this.dokumenterer = dokumenterer;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public String getFilename() {
        return filename;
    }

    public String getInnsendingsType() {
        return innsendingsType;
    }

    public String getSkjemanummer() {
        return skjemanummer;
    }

    public String getUuid() {
        return uuid;
    }

    public Dokumenterer getDokumenterer() {
        return dokumenterer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VedleggDto that = (VedleggDto) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "VedleggDto{" + "beskrivelse='" + beskrivelse + '\'' + ", filename='" + filename + '\'' + ", hvaDokumentererVedlegg="
            + dokumenterer + ", innsendingsType='" + innsendingsType + '\'' + ", skjemanummer='" + skjemanummer + '\'' + ", uuid='" + uuid
            + '\'' + '}';
    }

    public record Dokumenterer(@NotNull Type type,
                               @Valid ArbeidsforholdDto arbeidsforhold,
                               @Valid @Size(max = 100) List<@Valid @NotNull ÅpenPeriodeDto> perioder) {
        public enum Type {
            UTTAK,
            TILRETTELEGGING,
        }
    }
}
