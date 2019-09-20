package no.nav.foreldrepenger.selvbetjening.error;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;

import org.springframework.util.unit.DataSize;

public class AttachmentsTooLargeException extends AttachmentException {

    public AttachmentsTooLargeException(long size, DataSize max) {
        this(msg(size, max));
    }

    public AttachmentsTooLargeException(String msg) {
        super(msg);
    }

    private static String msg(long attachmentSize, DataSize max) {
        return format("Vedlegg-størrelse er %s, men kan ikke overstige %s",
                byteCountToDisplaySize(attachmentSize),
                byteCountToDisplaySize(max.toBytes()));
    }
}
