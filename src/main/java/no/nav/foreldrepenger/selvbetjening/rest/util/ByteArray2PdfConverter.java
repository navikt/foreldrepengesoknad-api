package no.nav.foreldrepenger.selvbetjening.rest.util;

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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class ByteArray2PdfConverter {

    private final List<MediaType> mediaTypes;

    public ByteArray2PdfConverter() {
        this(IMAGE_JPEG, IMAGE_PNG);
    }

    public ByteArray2PdfConverter(MediaType... mediaTypes) {
        this(Arrays.asList(mediaTypes));
    }

    public ByteArray2PdfConverter(List<MediaType> mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    private static final Logger LOG = LoggerFactory.getLogger(ByteArray2PdfConverter.class);

    public byte[] convert(String classPathResource) {
        try {
            return convert(copyToByteArray(new ClassPathResource(classPathResource).getInputStream()));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public byte[] convert(byte[] bytes) {
        return shouldConvert(bytes) ? doConvert(bytes) : bytes;
    }

    private byte[] doConvert(byte[] bytes) {
        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
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

    private boolean shouldConvert(byte[] bytes) {
        MediaType mediaType = MediaType.valueOf(new Tika().detect(bytes));
        boolean shouldConvert = mediaTypes.contains(mediaType);
        LOG.info("{} convert byte stream of type {} to PDF", shouldConvert ? "Will" : "Will not", mediaType);
        return shouldConvert;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [mediaTypes=" + mediaTypes + "]";
    }
}
