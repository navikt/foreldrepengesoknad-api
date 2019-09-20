package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

public interface VirusScanner {

    void scan(Vedlegg vedlegg);

    void scan(Attachment vedlegg);

}
