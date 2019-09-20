package no.nav.foreldrepenger.selvbetjening.error;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;

import org.springframework.util.unit.DataSize;

public class AttachmentsTooLargeException extends AttachmentException {
    public AttachmentsTooLargeException(long attachmentSize, DataSize max) {
        this(
                format(format("Samlet filstørrelse for alle vedlegg er %s, men må være mindre enn %s",
                        byteCountToDisplaySize(attachmentSize),
                        byteCountToDisplaySize(max.toBytes()))));
    }

    public AttachmentsTooLargeException(String msg) {
        super(msg);
    }

}
