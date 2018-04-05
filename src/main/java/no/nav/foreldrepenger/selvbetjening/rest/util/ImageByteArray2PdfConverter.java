package no.nav.foreldrepenger.selvbetjening.rest.util;

import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;
import static org.springframework.util.StreamUtils.copyToByteArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class ImageByteArray2PdfConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ImageByteArray2PdfConverter.class);

    private final List<MediaType> supportedMediaTypes;

    public ImageByteArray2PdfConverter() {
        this(IMAGE_JPEG, IMAGE_PNG);
    }

    public ImageByteArray2PdfConverter(MediaType... mediaTypes) {
        this(Arrays.asList(mediaTypes));
    }

    public ImageByteArray2PdfConverter(List<MediaType> mediaTypes) {
        this.supportedMediaTypes = mediaTypes;
    }

    public byte[] convert(String classPathResource) {
        return convert(classPathResource, false);
    }

    public byte[] convert(String classPathResource, boolean compress) {
        try {
            return convert(new ClassPathResource(classPathResource), compress);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public byte[] convert(Resource resource) throws IOException {
        return convert(resource, false);
    }

    public byte[] convert(Resource resource, boolean compress) throws IOException {
        return convert(copyToByteArray(resource.getInputStream()), compress);
    }

    public byte[] convert(byte[] bytes) {
        return convert(bytes, false);
    }

    public byte[] convert(byte[] bytes, boolean compress) {
        MediaType mediaType = mediaType(bytes);
        if (APPLICATION_PDF.equals(mediaType)) {
            LOG.info("Innhold er allerede PDF, konverteres ikke");
            return bytes;
        }
        if (shouldConvert(mediaType)) {
            return doConvert(bytes, compress);
        }
        throw new UnsupportedAttachmentTypeException(mediaType);
    }

    private byte[] doConvert(byte[] bytes, boolean compress) {
        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            if (compress) {
                writer.setFullCompression();
            }
            document.open();
            Image image = Image.getInstance(bytes);
            image.setAlignment(Image.ALIGN_CENTER);
            document.add(image);
            document.close();
            byte[] converted = baos.toByteArray();
            LOG.debug("Konverterert byte array av lengde {} til ny av lengde {}", bytes.length, converted.length);
            return converted;
        } catch (Exception e) {
            LOG.warn("Konvertering feilet");
            throw new IllegalArgumentException(e);
        }
    }

    private boolean shouldConvert(MediaType mediaType) {
        boolean shouldConvert = supportedMediaTypes.contains(mediaType);
        LOG.info("{} convert byte stream of type {} to PDF", shouldConvert ? "Will" : "Will not", mediaType);
        return shouldConvert;
    }

    private MediaType mediaType(byte[] bytes) {
        return MediaType.valueOf(new Tika().detect(bytes));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [supportedMediaTypes=" + supportedMediaTypes + "]";
    }
}
