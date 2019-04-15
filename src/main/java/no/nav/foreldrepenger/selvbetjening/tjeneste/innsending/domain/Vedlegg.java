package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import java.net.URI;

public class Vedlegg {

    public String id;
    public String skjemanummer;
    public String uuid;
    public URI url;
    public byte[] content;
    public String innsendingsType;
    public String beskrivelse;

    public Vedlegg kopi() {
        Vedlegg kopi = new Vedlegg();
        kopi.id = this.id;
        kopi.skjemanummer = this.skjemanummer;
        kopi.uuid = this.uuid;
        kopi.url = this.url;
        kopi.innsendingsType = this.innsendingsType;
        kopi.beskrivelse = this.beskrivelse;

        return kopi;
    }
}
