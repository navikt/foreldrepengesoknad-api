package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import no.nav.foreldrepenger.selvbetjening.vedlegg.AttachmentException;

import org.springframework.context.MessageSource;

public class AttachmentEmptyException extends AttachmentException {
    protected AttachmentEmptyException(String msg) {
        super(msg);
    }

    @Override
    public String getUserfacingErrorMessage(MessageSource messageSource) {
        return getMessage(messageSource, "vedlegg.tomtvedlegg");
    }
}
