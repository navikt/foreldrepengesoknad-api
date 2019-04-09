package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static java.util.Arrays.asList;
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
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentConversionException;
import no.nav.foreldrepenger.selvbetjening.error.AttachmentTypeUnsupportedException;

@Component
public class Image2PDFConverter {

    private final List<MediaType> supportedMediaTypes;

    private static final PDRectangle A4 = PDRectangle.A4;

    private static final Logger LOG = LoggerFactory.getLogger(Image2PDFConverter.class);

    @Inject
    public Image2PDFConverter() {
        this(IMAGE_JPEG, IMAGE_PNG);
    }

    Image2PDFConverter(MediaType... mediaTypes) {
        this(asList(mediaTypes));
    }

    private Image2PDFConverter(List<MediaType> mediaTypes) {
        this.supportedMediaTypes = mediaTypes;
    }

    byte[] convert(String classPathResource) {
        try {
            return convert(new ClassPathResource(classPathResource));
        } catch (Exception e) {
            throw new AttachmentConversionException("Kunne ikke konvertere vedlegg " + classPathResource, e);
        }
    }

    byte[] convert(Resource resource) throws IOException {
        return convert(copyToByteArray(resource.getInputStream()));
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
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            images.forEach(i -> addPDFPageFromImage(doc, i, imgType));
            doc.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", ex);
        }
    }

    private boolean validImageTypes(MediaType mediaType) {
        boolean validImageTypes = supportedMediaTypes.contains(mediaType);
        LOG.info("{} konvertere bytes av type {} til PDF", validImageTypes ? "Vil" : "Vil ikke", mediaType);
        return validImageTypes;
    }

    private static MediaType mediaType(byte[] bytes) {
        return MediaType.valueOf(new Tika().detect(bytes));
    }

    private static void addPDFPageFromImage(PDDocument doc, byte[] origImg, String imgFormat) {
        PDPage page = new PDPage(A4);
        doc.addPage(page);
        byte[] scaledImg = ImageScaler.downToA4(origImg, imgFormat);
        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
            PDImageXObject ximage = PDImageXObject.createFromByteArray(doc, scaledImg, "img");
            contentStream.drawImage(ximage, (int) A4.getLowerLeftX(), (int) A4.getLowerLeftY());
        } catch (Exception ex) {
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", ex);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [supportedMediaTypes=" + supportedMediaTypes + "]";
    }
}
