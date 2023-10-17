package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;

public class VedleggDto {

    private byte[] content;
    @Pattern(regexp = FRITEKST)
    private final String beskrivelse;

    @Valid
    private final MutableVedleggReferanseDto id;
    @Pattern(regexp = "^[\\p{Digit}\\p{L}_]*$")
    private final String innsendingsType;
    @Pattern(regexp = "^[\\p{Digit}\\p{L}]*$")
    private final String skjemanummer;
    @Pattern(regexp = FRITEKST)
    private final String uuid;
    private final URI url;
    @Digits(integer = 8, fraction = 0)
    private final Integer filesize;

    public VedleggDto(byte[] content, String beskrivelse, MutableVedleggReferanseDto id, String skjemanummer) {
        this(content, beskrivelse, id, null, skjemanummer, null, null, null);
    }

    @JsonCreator
    public VedleggDto(byte[] content, String beskrivelse, MutableVedleggReferanseDto id, String innsendingsType, String skjemanummer, String uuid, URI url,
                      Integer filesize) {
        this.content = content;
        this.beskrivelse = beskrivelse;
        this.id = id;
        this.innsendingsType = innsendingsType;
        this.skjemanummer = skjemanummer;
        this.uuid = uuid;
        this.url = url;
        this.filesize = filesize;
    }

    public VedleggDto kopi() {
        return new VedleggDto(null,
            this.getBeskrivelse(),
            this.getId(),
            this.getInnsendingsType(),
            this.getSkjemanummer(),
            this.getUuid(),
            this.getUrl(),
            this.getFilesize());
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

    public Integer getFilesize() {
        return filesize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VedleggDto that = (VedleggDto) o;
        return Arrays.equals(content, that.content) &&
            Objects.equals(innsendingsType, that.innsendingsType) &&
            Objects.equals(skjemanummer, that.skjemanummer) &&
            Objects.equals(filesize, that.filesize);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(innsendingsType, skjemanummer, filesize);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    @Override
    public String toString() {
        return "VedleggFrontend{" + "beskrivelse='" + beskrivelse + '\'' + ", id=" + id + ", innsendingsType='" + innsendingsType + '\''
            + ", skjemanummer='" + skjemanummer + '\'' + ", uuid='" + uuid + '\'' + ", url=" + url + ", filesize=" + filesize + '}';
    }
}
