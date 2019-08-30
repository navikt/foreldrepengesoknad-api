package no.nav.foreldrepenger.selvbetjening.error;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;
import org.springframework.http.MediaType;

public class AttachmentPasswordProtectedException extends AttachmentException {

    public AttachmentPasswordProtectedException(Attachment attachment) {
        super(attachment.filename + " er en kryptert PDF");
    }
}
