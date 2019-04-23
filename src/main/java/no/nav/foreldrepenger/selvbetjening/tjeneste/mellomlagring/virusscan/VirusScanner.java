package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.virusscan;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

public interface VirusScanner {
    boolean scan(Attachment attachment);
}
