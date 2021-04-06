package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static java.util.Arrays.asList;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.ImageScaler.downToA4;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggUtil.mediaType;
import static org.apache.pdfbox.pdmodel.common.PDRectangle.A4;
import static org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject.createFromByteArray;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;
import static org.springframework.util.StreamUtils.copyToByteArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class Image2PDFConverter {

    private final List<MediaType> supportedMediaTypes;

    private static final Logger LOG = LoggerFactory.getLogger(Image2PDFConverter.class);

    @Inject
    public Image2PDFConverter() {
        this(IMAGE_JPEG, IMAGE_PNG);
    }

    Image2PDFConverter(MediaType... supportedMediaTypes) {
        this(asList(supportedMediaTypes));
    }

    private Image2PDFConverter(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    byte[] convert(String classPathResource) {
        try {
            return convert(new ClassPathResource(classPathResource));
        } catch (AttachmentException e) {
            throw e;
        } catch (Exception e) {
            throw new AttachmentConversionException("Kunne ikke konvertere vedlegg " + classPathResource, e);
        }
    }

    byte[] convert(Resource res) throws IOException {
        return convert(copyToByteArray(res.getInputStream()));
    }

    public byte[] convert(byte[] bytes) {
        MediaType mediaType = mediaType(bytes);
        if (APPLICATION_PDF.equals(mediaType)) {
            return bytes;
        }
        if (validImageTypes(mediaType)) {
            return embedImagesInPdf(mediaType.getSubtype(), bytes);
        }
        throw new AttachmentTypeUnsupportedException(mediaType);
    }

    private static byte[] embedImagesInPdf(String imgType, byte[]... images) {
        return embedImagesInPdf(asList(images), imgType);
    }

    private static byte[] embedImagesInPdf(List<byte[]> images, String imgType) {
        try (var doc = new PDDocument(); var outputStream = new ByteArrayOutputStream()) {
            images.forEach(i -> addPDFPageFromImage(doc, i, imgType));
            doc.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", e);
        }
    }

    private boolean validImageTypes(MediaType mediaType) {
        boolean isValid = supportedMediaTypes.contains(mediaType);
        LOG.info("{} konvertere bytes av type {} til PDF", isValid ? "Vil" : "Vil ikke", mediaType);
        return isValid;
    }

    private static void addPDFPageFromImage(PDDocument doc, byte[] orig, String fmt) {
        PDPage page = new PDPage(A4);
        doc.addPage(page);
        try (var cs = new PDPageContentStream(doc, page)) {
            cs.drawImage(createFromByteArray(doc, downToA4(orig, fmt), "img"), (int) A4.getLowerLeftX(), (int) A4.getLowerLeftY());
        } catch (Exception e) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [supportedMediaTypes=" + supportedMediaTypes + "]";
    }
}
