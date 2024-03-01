package no.nav.foreldrepenger.selvbetjening.vedlegg;

import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;

public interface VedleggSjekker {
    void sjekk(Attachment... vedlegg);
}
