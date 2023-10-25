package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.springframework.context.MessageSource;

public class AttachmentVirusException extends AttachmentException {

    public AttachmentVirusException(String uuid) {
        super(uuid);
    }

    @Override
    public String getUserfacingErrorMessage(MessageSource messageSource) {
        return getMessage(messageSource, "vedlegg.virus");
    }
}
