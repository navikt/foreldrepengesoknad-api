package no.nav.foreldrepenger.selvbetjening.vedlegg;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;

public interface VedleggSjekker {
    void sjekk(VedleggDto... vedlegg);

    void sjekk(Attachment... vedlegg);
}
