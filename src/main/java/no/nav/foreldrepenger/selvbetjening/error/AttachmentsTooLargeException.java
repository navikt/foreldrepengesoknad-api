package no.nav.foreldrepenger.selvbetjening.error;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;

public class AttachmentsTooLargeException extends AttachmentException {

    public AttachmentsTooLargeException(long size, long max) {
        this(msg(size, max));
    }

    public AttachmentsTooLargeException(String msg) {
        super(msg);
    }

    private static String msg(long attachmentSize, long max) {
        return format("Vedlegg-størrelse er %s, men kan ikke overstige %s",
                byteCountToDisplaySize(attachmentSize),
                byteCountToDisplaySize(max));
    }
}
