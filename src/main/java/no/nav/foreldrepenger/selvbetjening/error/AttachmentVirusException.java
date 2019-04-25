package no.nav.foreldrepenger.selvbetjening.error;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment;

public class AttachmentVirusException extends AttachmentException {

    private final Attachment attachment;

    public AttachmentVirusException(Attachment attachment) {
        super(attachment.filename);
        this.attachment = attachment;
    }

    public Attachment getAttachment() {
        return attachment;
    }
}
