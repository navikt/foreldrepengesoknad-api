package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;

public interface VirusScanner {

    void scan(Vedlegg vedlegg);

}
