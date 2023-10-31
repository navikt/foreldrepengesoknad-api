package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.net.URI;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.Valid;
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

    public VedleggDto(byte[] content, String beskrivelse, MutableVedleggReferanseDto id, String skjemanummer) {
        this(content, beskrivelse, id, null, skjemanummer, null, null);
    }

    @JsonCreator
    public VedleggDto(byte[] content, String beskrivelse, MutableVedleggReferanseDto id, String innsendingsType, String skjemanummer, String uuid, URI url) {
        this.content = content;
        this.beskrivelse = beskrivelse;
        this.id = id;
        this.innsendingsType = innsendingsType;
        this.skjemanummer = skjemanummer;
        this.uuid = uuid;
        this.url = url;
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
}
