package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static java.lang.String.format;

import org.springframework.util.unit.DataSize;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggDto;


public class AttachmentTooLargeException extends AttachmentException {

    public AttachmentTooLargeException(VedleggDto vedlegg, DataSize max) {
        this(DataSize.ofBytes(vedlegg.getContent().length), max);
    }

    public AttachmentTooLargeException(DataSize size, DataSize maxVedleggSize) {
        this(msg(size, maxVedleggSize));
    }

    private AttachmentTooLargeException(String msg) {
        super(msg);
    }

    private static String msg(DataSize attachmentSize, DataSize max) {
        return format("Vedlegg-størrelse er %s MB, men kan ikke overstige %s MB",
                attachmentSize.toMegabytes(), max.toMegabytes());
    }
}
