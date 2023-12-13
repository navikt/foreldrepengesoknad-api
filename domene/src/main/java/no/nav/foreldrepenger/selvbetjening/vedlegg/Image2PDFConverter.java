package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static no.nav.foreldrepenger.selvbetjening.vedlegg.ImageScaler.pdfFraBilde;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggUtil.mediaType;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

@Component
public class Image2PDFConverter {
    private static final Logger LOG = LoggerFactory.getLogger(Image2PDFConverter.class);

    private final List<MediaType> supportedMediaTypes;

    public Image2PDFConverter() {
        this(List.of(IMAGE_JPEG, IMAGE_PNG));
    }

    Image2PDFConverter(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }


    public byte[] convert(byte[] bytes) {
        var mediaType = mediaType(bytes);
        if (APPLICATION_PDF.equals(mediaType)) {
            return bytes;
        }

        if (supportedMediaTypes.contains(mediaType)) {
            long start = System.currentTimeMillis();
            var pdfBytes = konverterBildeTilPdf(bytes, mediaType);
            long slutt = System.currentTimeMillis();
            LOG.info("Konvertering av {} med størrelse {}MB til PDF med størrelse {}MB tok {}ms", mediaType, megabytes(bytes), megabytes(pdfBytes), slutt - start);
            return pdfBytes;
        }
        throw new AttachmentTypeUnsupportedException(mediaType);
    }

    public static BigDecimal megabytes(byte[] bytes) {
        return BigDecimal.valueOf(DataSize.ofBytes(bytes.length).toKilobytes())
            .divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
    }

    private static byte[] konverterBildeTilPdf(byte[] innhold, MediaType mediaType) {
        try (var doc = new PDDocument(); var outputStream = new ByteArrayOutputStream()) {
            addPDFPageFromImage(doc, innhold, mediaType);
            doc.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", e);
        }
    }

    private static void addPDFPageFromImage(PDDocument doc, byte[] orig, MediaType mediaType) {
        try {
           pdfFraBilde(doc, orig, mediaType);
        } catch (Exception e) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [supportedMediaTypes=" + supportedMediaTypes + "]";
    }
}
