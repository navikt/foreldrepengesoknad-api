package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.rest.attachments.exceptions.AttachmentConversionException;

@Component
public class PDFPageSplitter {

    List<byte[]> split(String resource) {
        return split(new ClassPathResource(resource));
    }

    List<byte[]> split(Resource resource) {
        try {
            return split(resource.getInputStream());
        } catch (IOException e) {
            throw new AttachmentConversionException("Kunne ikke splitte " + resource, e);
        }
    }

    public List<byte[]> split(byte[] bytes) {
        return split(new ByteArrayInputStream(bytes));
    }

    List<byte[]> split(InputStream stream) {
        try (PDDocument document = PDDocument.load(stream)) {
            return split(document).stream()
                    .map(PDFPageSplitter::toByteArray)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            throw new AttachmentConversionException("Kunne ikke splitte PDF", ex);
        }
    }

    private static List<PDDocument> split(PDDocument document) {
        try {
            return new Splitter().split(document);
        } catch (IOException e) {
            throw new AttachmentConversionException("Kunne ikke splitte PDF", e);
        }
    }

    private static byte[] toByteArray(PDDocument page) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            page.save(baos);
            page.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new AttachmentConversionException("Kunne ikke ekstrahere bytes fra PDF", e);
        }
    }
}
