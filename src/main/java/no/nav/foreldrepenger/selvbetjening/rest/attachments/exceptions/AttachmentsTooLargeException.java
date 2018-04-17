package no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions;

public class AttachmentsTooLargeException extends AttachmentException {

    public AttachmentsTooLargeException(long size) {
        super("Samlet filstørrelse for alle vedlegg kan ikke overstige " + String.valueOf(size) + " bytes");
    }
}
