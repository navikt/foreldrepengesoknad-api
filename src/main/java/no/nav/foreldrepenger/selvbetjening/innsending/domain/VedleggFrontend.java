package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.net.URI;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class VedleggFrontend {

    private byte[] content;
    @Pattern(regexp = FRITEKST)
    private final String beskrivelse;
    @Pattern(regexp = FRITEKST)
    private final String id;
    @Pattern(regexp = FRITEKST)
    private final String innsendingsType;
    @Pattern(regexp = FRITEKST)
    private final String skjemanummer;
    @Pattern(regexp = FRITEKST)
    private final String uuid;
    private final URI url;

    public VedleggFrontend(byte[] content, String beskrivelse, String id, String skjemanummer) {
        this(content, beskrivelse, id, null, skjemanummer, null, null);
    }

    @JsonCreator
    public VedleggFrontend(byte[] content, String beskrivelse, String id, String innsendingsType, String skjemanummer, String uuid, URI url) {
        this.content = content;
        this.beskrivelse = beskrivelse;
        this.id = id;
        this.innsendingsType = innsendingsType;
        this.skjemanummer = skjemanummer;
        this.uuid = uuid;
        this.url = url;
    }

    public VedleggFrontend kopi() {
        return new VedleggFrontend(null,
            this.getBeskrivelse(),
            this.getId(),
            this.getInnsendingsType(),
            this.getSkjemanummer(),
            this.getUuid(),
            this.getUrl());
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

    public String getId() {
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
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", skjemanummer=" + skjemanummer + ", uuid=" + uuid
                + ", url=" + url + ", innsendingsType=" + innsendingsType + "]";
    }
}
