package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static java.util.Arrays.asList;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.ImageScaler.pdfFraBilde;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggUtil.mediaType;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;

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
        if (validImageTypes(mediaType)) {
            return embedImagesInPdf(bytes);
        }
        throw new AttachmentTypeUnsupportedException(mediaType);
    }

    private static byte[] embedImagesInPdf(byte[]... images) {
        return embedImagesInPdf(asList(images));
    }

    private static byte[] embedImagesInPdf(List<byte[]> images) {
        try (var doc = new PDDocument(); var outputStream = new ByteArrayOutputStream()) {
            images.forEach(i -> addPDFPageFromImage(doc, i));
            doc.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", e);
        }
    }

    private boolean validImageTypes(MediaType mediaType) {
        var isValid = supportedMediaTypes.contains(mediaType);
        LOG.info("{} konvertere bytes av type {} til PDF", isValid ? "Vil" : "Vil ikke", mediaType);
        return isValid;
    }

    private static void addPDFPageFromImage(PDDocument doc, byte[] orig) {
        try {
           pdfFraBilde(doc, orig);
        } catch (Exception e) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [supportedMediaTypes=" + supportedMediaTypes + "]";
    }
}
