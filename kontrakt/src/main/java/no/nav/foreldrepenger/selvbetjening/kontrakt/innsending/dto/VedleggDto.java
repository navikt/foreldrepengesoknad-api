package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;

public class VedleggDto {

    private byte[] content;
    @Pattern(regexp = FRITEKST)
    private final String beskrivelse;

    @Valid
    private final Dokumenterer dokumenterer;

    @Deprecated
    @Valid
    private final MutableVedleggReferanseDto id;
    @Pattern(regexp = "^[\\p{Digit}\\p{L}_]*$")
    private final String innsendingsType;
    @Pattern(regexp = "^[\\p{Digit}\\p{L}]*$")
    private final String skjemanummer;
    @Pattern(regexp = FRITEKST)
    private final String uuid;
    @Deprecated
    private final URI url;


    public VedleggDto(byte[] content, String beskrivelse, MutableVedleggReferanseDto id, String skjemanummer) {
        this(content, beskrivelse, id, null, skjemanummer, null, null, null);
    }

    @JsonCreator
    public VedleggDto(byte[] content, String beskrivelse, MutableVedleggReferanseDto id, String innsendingsType, String skjemanummer, String uuid, URI url,
                      Dokumenterer dokumenterer) {
        this.content = content;
        this.beskrivelse = beskrivelse;
        this.id = id;
        this.innsendingsType = innsendingsType;
        this.skjemanummer = skjemanummer;
        this.uuid = uuid;
        this.url = url;
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

    public MutableVedleggReferanseDto getId() {
        return id;
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

    public URI getUrl() {
        return url;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "VedleggFrontend{" + "beskrivelse='" + beskrivelse + '\'' + ", id=" + id + ", innsendingsType='" + innsendingsType + '\''
            + ", skjemanummer='" + skjemanummer + '\'' + ", uuid='" + uuid + '\'' + ", url=" + url + '}';
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
