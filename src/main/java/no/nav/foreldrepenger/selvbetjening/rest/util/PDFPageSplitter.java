package no.nav.foreldrepenger.selvbetjening.rest.util;

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

@Component
public class PDFPageSplitter {

    public List<byte[]> split(String resource) {
        return split(new ClassPathResource(resource));
    }

    public List<byte[]> split(Resource resource) {
        try {
            return split(resource.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<byte[]> split(byte[] bytes) {
        return split(new ByteArrayInputStream(bytes));
    }

    public List<byte[]> split(InputStream stream) {
        try (PDDocument document = PDDocument.load(stream)) {
            return split(document).stream()
                    .map(PDFPageSplitter::toByteArray)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            throw new RuntimeException("Error while splitting PDF into pages", ex);
        }
    }

    private static List<PDDocument> split(PDDocument document) {
        try {
            return new Splitter().split(document);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] toByteArray(PDDocument page) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            page.save(baos);
            page.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
