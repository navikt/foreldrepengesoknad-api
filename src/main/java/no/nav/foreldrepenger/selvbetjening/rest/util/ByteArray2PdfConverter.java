package no.nav.foreldrepenger.selvbetjening.rest.util;

import static org.springframework.util.StreamUtils.copyToByteArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class ByteArray2PdfConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ByteArray2PdfConverter.class);

    public byte[] convert(String classPathResource) {
        try {
            return convert(copyToByteArray(new ClassPathResource(classPathResource).getInputStream()));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public byte[] convert(byte[] bytes) {
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
            throw new IllegalArgumentException(e);
        }
    }
}
