package no.nav.foreldrepenger.selvbetjening.innsending.json;

import java.util.ArrayList;
import java.util.List;

public class Ettersending {
    public String saksnummer;
    public List<Vedlegg> vedlegg;

    public Ettersending(){
        vedlegg = new ArrayList<>();
    }
}
