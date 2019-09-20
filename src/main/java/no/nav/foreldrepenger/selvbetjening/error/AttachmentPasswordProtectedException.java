package no.nav.foreldrepenger.selvbetjening.error;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;

public class AttachmentPasswordProtectedException extends AttachmentException {

    public AttachmentPasswordProtectedException(Vedlegg v, Exception e) {
        super(v.getUrl() + " er en kryptert PDF", e);
    }
}
