package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import java.net.URI;

import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

public class Vedlegg {

    private String id;
    private String skjemanummer;
    private String uuid;
    private URI url;
    private byte[] content;
    private String innsendingsType;
    private String beskrivelse;

    public Vedlegg kopi() {
        Vedlegg kopi = new Vedlegg();
        kopi.setId(this.getId());
        kopi.setSkjemanummer(this.getSkjemanummer());
        kopi.setUuid(this.getUuid());
        kopi.setUrl(this.getUrl());
        kopi.setInnsendingsType(this.getInnsendingsType());
        kopi.setBeskrivelse(this.getBeskrivelse());
        return kopi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkjemanummer() {
        return skjemanummer;
    }

    public void setSkjemanummer(String skjemanummer) {
        this.skjemanummer = skjemanummer;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public byte[] getContent() {
        return content;
    }

    public byte[] setContent(byte[] content) {
        this.content = content;
        return content;
    }

    public String getInnsendingsType() {
        return innsendingsType;
    }

    public void setInnsendingsType(String innsendingsType) {
        this.innsendingsType = innsendingsType;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", skjemanummer=" + skjemanummer + ", uuid=" + uuid
                + ", url=" + url + ", content=" + StringUtil.limit(content) + ", innsendingsType=" + innsendingsType
                + ", beskrivelse=" + beskrivelse + "]";
    }
}
