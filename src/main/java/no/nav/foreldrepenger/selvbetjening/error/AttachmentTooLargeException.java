package no.nav.foreldrepenger.selvbetjening.error;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;

import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;

public class AttachmentTooLargeException extends AttachmentException {

    public AttachmentTooLargeException(Vedlegg vedlegg, DataSize max) {
        this(vedlegg.getContent().length, max);
    }

    public AttachmentTooLargeException(String msg) {
        super(msg);
    }

    public AttachmentTooLargeException(long size, DataSize maxVedleggSize) {
        this(msg(size, maxVedleggSize));
    }

    private static String msg(long attachmentSize, DataSize max) {
        return format("Vedlegg-størrelse er %s, men kan ikke overstige %s",
                byteCountToDisplaySize(attachmentSize),
                byteCountToDisplaySize(max.toBytes()));
    }
}
