package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import java.util.ArrayList;
import java.util.List;

public class Ettersending {
    private String type;
    private String saksnummer;
    private List<Vedlegg> vedlegg;

    public Ettersending() {
        setVedlegg(new ArrayList<>());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    public void setSaksnummer(String saksnummer) {
        this.saksnummer = saksnummer;
    }

    public List<Vedlegg> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<Vedlegg> vedlegg) {
        this.vedlegg = vedlegg;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[type=" + type + ", saksnummer=" + saksnummer + ", vedlegg=" + vedlegg
                + "]";
    }
}
