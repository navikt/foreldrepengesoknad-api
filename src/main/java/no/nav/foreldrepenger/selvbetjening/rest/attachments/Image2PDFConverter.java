package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import static com.itextpdf.text.PageSize.A4;
import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;
import static org.springframework.util.StreamUtils.copyToByteArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentConversionException;
import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentTypeUnsupportedException;

@Component
public class Image2PDFConverter {

    private final PDFPageSplitter pdfPageSplitter;
    private final PDF2ImageConverter pdf2ImageConverter;
    private final List<MediaType> supportedMediaTypes;

    private static final Logger LOG = LoggerFactory.getLogger(Image2PDFConverter.class);

    @Inject
    public Image2PDFConverter(PDFPageSplitter splitter, PDF2ImageConverter converter) {
        this(splitter, converter, IMAGE_JPEG, IMAGE_PNG);
    }

    public Image2PDFConverter(PDFPageSplitter splitter, PDF2ImageConverter converter,
            MediaType... mediaTypes) {
        this(splitter, converter, asList(mediaTypes));
    }

    public Image2PDFConverter(PDFPageSplitter splitter, PDF2ImageConverter converter,
            List<MediaType> mediaTypes) {
        this.pdfPageSplitter = splitter;
        this.pdf2ImageConverter = converter;
        this.supportedMediaTypes = mediaTypes;
    }

    public byte[] convert(String classPathResource) {
        try {
            return convert(new ClassPathResource(classPathResource));
        } catch (IOException e) {
            throw new AttachmentConversionException("Kunne ikke konvertere vedlegg " + classPathResource, e);
        }
    }

    public byte[] convert(Resource resource) throws IOException {
        return convert(copyToByteArray(resource.getInputStream()));
    }

    public byte[] convert(byte[] bytes) {
        MediaType mediaType = mediaType(bytes);
        if (APPLICATION_PDF.equals(mediaType)) {
            LOG.info("Innhold er allerede PDF, deles opp i deler");
            List<byte[]> pdfPages = pdfPageSplitter.split(bytes);
            LOG.info("PDF ble delt opp i {} deler", pdfPages.size());
            return embedImagesInPdf(pdf2ImageConverter.convertToImages(pdfPages));
        }
        if (shouldConvertImage(mediaType)) {
            return embedImagesInPdf(bytes);
        }
        throw new AttachmentTypeUnsupportedException(mediaType);
    }

    private static byte[] embedImagesInPdf(byte[]... images) {
        return embedImagesInPdf(asList(images));
    }

    private static byte[] embedImagesInPdf(List<byte[]> images) {
        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setFullCompression();
            document.open();
            images.stream().forEach(s -> addImage(document, s));
            document.close();
            return baos.toByteArray();
        } catch (DocumentException e) {
            LOG.warn("Konvertering av vedlegg feilet", e);
            throw new AttachmentConversionException("Konvertering av vedlegg feilet", e);
        }
    }

    private static void addImage(Document document, byte[] bytes) {
        try {
            LOG.info("Legger image til i dokumentet");
            Image image = Image.getInstance(bytes);
            image.setAlignment(Element.ALIGN_CENTER);
            image.scaleToFit(A4.getWidth(), A4.getHeight());
            document.add(image);
        } catch (IOException | DocumentException e) {
            throw new AttachmentConversionException("Kunne ikke legge image til i dokument", e);
        }
    }

    private boolean shouldConvertImage(MediaType mediaType) {
        boolean shouldConvert = supportedMediaTypes.contains(mediaType);
        LOG.info("{} konvertere bytes av type {} til PDF", shouldConvert ? "Vil" : "Vil ikke", mediaType);
        return shouldConvert;
    }

    private static MediaType mediaType(byte[] bytes) {
        return MediaType.valueOf(new Tika().detect(bytes));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [splitter=" + pdfPageSplitter + ", supportedMediaTypes="
                + supportedMediaTypes
                + "]";
    }

}
