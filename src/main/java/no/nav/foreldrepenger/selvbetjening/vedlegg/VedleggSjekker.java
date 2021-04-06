package no.nav.foreldrepenger.selvbetjening.vedlegg;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;

public interface VedleggSjekker {
    void sjekk(Vedlegg... vedlegg);

    void sjekk(Attachment... vedlegg);
}
