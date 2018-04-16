package no.nav.foreldrepenger.selvbetjening.rest.attachments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class PDFMetadataExtractor {

    public no.nav.foreldrepenger.selvbetjening.rest.attachments.Metadata metadata(String classPathResource) {
        return metadata(new ClassPathResource(classPathResource));
    }

    private no.nav.foreldrepenger.selvbetjening.rest.attachments.Metadata metadata(Resource resource) {
        try {
            return metadata(resource.getInputStream());
        } catch (IOException e) {
            throw new AttachmentConversionException("Kunne ikke hente metadata fra " + resource, e);
        }
    }

    private no.nav.foreldrepenger.selvbetjening.rest.attachments.Metadata metadata(InputStream inputStream) {
        checkPdf(inputStream);
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        try {
            parser.parse(inputStream, handler, metadata, context);
            return new no.nav.foreldrepenger.selvbetjening.rest.attachments.Metadata(metadata);
        } catch (IOException | SAXException | TikaException e) {
            throw new AttachmentConversionException("oops", e);
        }

    }

    public void metdata(byte[] bytes) {
        new ByteArrayInputStream(bytes);
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
