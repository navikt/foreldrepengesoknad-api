package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

@Component
public class PDFMetadataExtractor {

    public PDFMetadata metadata(String classPathResource) {
        return metadata(new ClassPathResource(classPathResource));
    }

    private PDFMetadata metadata(Resource resource) {
        try {
            return metadata(resource.getInputStream());
        } catch (IOException e) {
            throw new AttachmentConversionException("Kunne ikke hente input stream fra " + resource, e);
        }
    }

    private PDFMetadata metadata(InputStream inputStream) {
        checkPdf(inputStream);
        Parser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler(-1);
        org.apache.tika.metadata.Metadata metadata = new org.apache.tika.metadata.Metadata();
        ParseContext context = new ParseContext();
        try {
            parser.parse(inputStream, handler, metadata, context);
            return new PDFMetadata(metadata);
        } catch (IOException | SAXException | TikaException e) {
            throw new AttachmentConversionException("Kunne ikke hente metadata", e);
        }
    }

    private static void checkPdf(InputStream inputStream) {
        try {
            MediaType mediaType = MediaType.valueOf(new Tika().detect(inputStream));
            if (!MediaType.APPLICATION_PDF.equals(mediaType)) {
                throw new UnsupportedAttachmentTypeException(mediaType);
            }
        } catch (IOException e) {
            throw new AttachmentConversionException("Kunne ikke sjekke attachment type", e);
        }
    }
}
