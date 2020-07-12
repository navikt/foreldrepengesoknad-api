package no.nav.foreldrepenger.selvbetjening.virusscan;

import no.nav.foreldrepenger.selvbetjening.http.Pingable;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;

public interface VirusScanner extends Pingable, RetryAware {

    void scan(Vedlegg vedlegg);

    void scan(Attachment vedlegg);

}
