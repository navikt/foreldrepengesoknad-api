package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static java.lang.String.format;

import org.springframework.util.unit.DataSize;

public class AttachmentsTooLargeException extends AttachmentException {
    public AttachmentsTooLargeException(long attachmentSize, DataSize max) {
        this(format(format("Samlet filstørrelse for alle vedlegg er %s Mb, men må være mindre enn %s", attachmentSize,
                max)));
    }

    public AttachmentsTooLargeException(String msg) {
        super(msg);
    }

}
