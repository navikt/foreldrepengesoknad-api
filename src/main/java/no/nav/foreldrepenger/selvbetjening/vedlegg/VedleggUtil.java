package no.nav.foreldrepenger.selvbetjening.vedlegg;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.IOException;

public class VedleggUtil {

    private static final Logger LOG = LoggerFactory.getLogger(VedleggUtil.class);

    public static boolean isPdfEncrypted(byte[] ba) {
        if (!MediaType.APPLICATION_PDF.equals(mediaType(ba))) {
            return false;
        }

        try (PDDocument doc = PDDocument.load(ba)) {
            return false;
        } catch (InvalidPasswordException e) {
            return true;
        } catch (IOException e) {
            LOG.warn("Lesing av pdf feilet. Fortsetter uten krypteringssjekk.", e);
            return false;
        }
    }

    static MediaType mediaType(byte[] bytes) {
        return MediaType.valueOf(new Tika().detect(bytes));
    }

}
