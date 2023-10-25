package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.springframework.context.MessageSource;

public class AttachmentUnreadableException extends AttachmentException {
    protected AttachmentUnreadableException(String msg) {
        super(msg);
    }

    @Override
    public String getUserfacingErrorMessage(MessageSource messageSource) {
        return getMessage(messageSource, "vedlegg.korrumpert");
    }
}
