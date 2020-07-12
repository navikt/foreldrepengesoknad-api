package no.nav.foreldrepenger.selvbetjening.tjeneste.virusscan;

import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

public interface VirusScanner extends Pingable, RetryAware {

    void scan(Vedlegg vedlegg);

    void scan(Attachment vedlegg);

}
