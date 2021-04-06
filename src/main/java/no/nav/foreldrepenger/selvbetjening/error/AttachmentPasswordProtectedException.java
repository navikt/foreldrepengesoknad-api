package no.nav.foreldrepenger.selvbetjening.error;

public class AttachmentPasswordProtectedException extends AttachmentException {

    public AttachmentPasswordProtectedException(String name, Exception e) {
        super(name + " er en kryptert PDF", e);
    }
}
