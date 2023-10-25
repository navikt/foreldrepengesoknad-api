package no.nav.foreldrepenger.selvbetjening.vedlegg;


import org.springframework.context.MessageSource;

public class AttachmentPasswordProtectedException extends AttachmentException {

    public AttachmentPasswordProtectedException() {
        super("Dokumentet kan ikke håndteres av NAV ettersom det er passordbeskyttet.");
    }

    @Override
    public String getUserfacingErrorMessage(MessageSource messageSource) {
        return getMessage(messageSource, "vedlegg.passordbeskyttet");
    }
}
