package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggUtil.mediaType;
import static org.springframework.http.MediaType.APPLICATION_PDF;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentPasswordProtectedException;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;

@Component
public class PDFEncryptionChecker {

    private static final Logger LOG = LoggerFactory.getLogger(VedleggSjekker.class);

    public void checkEncrypted(Vedlegg v) {
        if (APPLICATION_PDF.equals(mediaType(v.getContent()))) {
            try (PDDocument doc = PDDocument.load(v.getContent())) {
            } catch (InvalidPasswordException e) {
                throw new AttachmentPasswordProtectedException(v, e);
            } catch (Exception e) {
                LOG.warn("Kunne ikke sjekke {}", v, e);
            }
        }
    }

}
