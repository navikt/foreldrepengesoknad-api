package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

public class AttachmentPasswordProtectedException extends AttachmentException {

    public AttachmentPasswordProtectedException(InvalidPasswordException e) {
        super("Kryptert PDF", e);
    }
}
