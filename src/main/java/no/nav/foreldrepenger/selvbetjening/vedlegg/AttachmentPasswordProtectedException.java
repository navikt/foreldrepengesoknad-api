package no.nav.foreldrepenger.selvbetjening.vedlegg;

public class AttachmentPasswordProtectedException extends AttachmentException {

    public AttachmentPasswordProtectedException(String name, Exception e) {
        super(name + " er en kryptert PDF", e);
    }
}
