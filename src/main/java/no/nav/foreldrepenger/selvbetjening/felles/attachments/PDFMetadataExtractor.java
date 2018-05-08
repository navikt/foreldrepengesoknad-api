package no.nav.foreldrepenger.selvbetjening.felles.attachments;

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
import org.xml.sax.SAXException;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentConversionException;
import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentTypeUnsupportedException;

@Component
public class PDFMetadataExtractor {

    PDFMetadata metadata(String classPathResource) {
        return metadata(new ClassPathResource(classPathResource));
    }

    private PDFMetadata metadata(Resource resource) {
        try {
            return metadata(resource.getInputStream());
        } catch (IOException e) {
            throw new AttachmentConversionException("Kunne ikke hente input stream fra " + resource, e);
        }
    }

    public PDFMetadata metadata(InputStream inputStream) {
        checkIsPdf(inputStream);
        Parser parser = new AutoDetectParser();
        org.apache.tika.metadata.Metadata metadata = new org.apache.tika.metadata.Metadata();
        try {
            parser.parse(inputStream, new BodyContentHandler(-1), metadata, new ParseContext());
            return new PDFMetadata(metadata);
        } catch (IOException | SAXException | TikaException e) {
            throw new AttachmentConversionException("Kunne ikke hente metadata", e);
        }
    }

    private static void checkIsPdf(InputStream inputStream) {
        try {
            MediaType mediaType = MediaType.valueOf(new Tika().detect(inputStream));
            if (!MediaType.APPLICATION_PDF.equals(mediaType)) {
                throw new AttachmentTypeUnsupportedException(mediaType);
            }
        } catch (IOException e) {
            throw new AttachmentTypeUnsupportedException(e);
        }
    }
}
