package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain;

import java.util.ArrayList;
import java.util.List;

public class Ettersending {
    public String saksnummer;
    public List<Vedlegg> vedlegg;

    public Ettersending(){
        vedlegg = new ArrayList<>();
    }
}
