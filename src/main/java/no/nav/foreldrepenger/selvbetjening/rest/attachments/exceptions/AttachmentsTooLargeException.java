package no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions;

public class AttachmentsTooLargeException extends AttachmentException {

    private final long size;

    public AttachmentsTooLargeException(long size) {
        super(null);
        this.size = size;
    }

    public long getSize() {
        return size;
    }
}
