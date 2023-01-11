package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

public class AttachmentPasswordProtectedException extends AttachmentException {

    public AttachmentPasswordProtectedException() {
        super("Dokumentet kan ikke håndteres av NAV ettersom det er passordbeskyttet.");
    }
}
