package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.EqualsAndHashCode;
import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

@EqualsAndHashCode
public class Vedlegg {

    private byte[] content;
    private final String beskrivelse;
    private final String id;
    private final String innsendingsType;
    private final String skjemanummer;
    private final String uuid;
    private final URI url;

    public Vedlegg(byte[] content, String beskrivelse, String id, String skjemanummer) {
        this(content, beskrivelse, id, null, skjemanummer, null, null);
    }

    @JsonCreator
    public Vedlegg(byte[] content, String beskrivelse, String id, String innsendingsType, String skjemanummer, String uuid, URI url) {
        this.content = content;
        this.beskrivelse = beskrivelse;
        this.id = id;
        this.innsendingsType = innsendingsType;
        this.skjemanummer = skjemanummer;
        this.uuid = uuid;
        this.url = url;
    }

    public Vedlegg kopi() {
        return new Vedlegg(null,
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
                + ", url=" + url + ", content=" + StringUtil.limit(content) + ", innsendingsType=" + innsendingsType
                + ", beskrivelse=" + beskrivelse + "]";
    }
}
