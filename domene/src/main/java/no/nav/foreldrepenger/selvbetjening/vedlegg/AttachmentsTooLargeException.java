package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static java.lang.String.format;

import org.springframework.context.MessageSource;
import org.springframework.util.unit.DataSize;

public class AttachmentsTooLargeException extends AttachmentException {
    private final long attachmentSize;
    private final DataSize max;

    public AttachmentsTooLargeException(long attachmentSize, DataSize max) {
        super(format("Samlet filstørrelse for alle vedlegg er %s Mb, men må være mindre enn %s", attachmentSize, max));
        this.attachmentSize = attachmentSize;
        this.max = max;
    }

    @Override
    public String getUserfacingErrorMessage(MessageSource messageSource) {
        return getMessage(messageSource, "vedlegg.samlet.for.stort", attachmentSize, max);
    }
}
