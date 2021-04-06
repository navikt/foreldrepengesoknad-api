package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggUtil.mediaType;
import static org.springframework.http.MediaType.APPLICATION_PDF;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;
import no.nav.foreldrepenger.selvbetjening.util.StringUtil;

@Component
public class PDFEncryptionChecker {

    private static final Logger LOG = LoggerFactory.getLogger(VedleggSjekker.class);

    public void checkEncrypted(Attachment v) {
        check(v.bytes);
    }

    public void sjekkKryptert(Vedlegg v) {
        check(v.getContent());
    }

    private static void check(byte[] bytes) {
        if (bytes != null && APPLICATION_PDF.equals(mediaType(bytes))) {
            try (var doc = PDDocument.load(bytes)) {
            } catch (InvalidPasswordException e) {
                throw new AttachmentPasswordProtectedException(e);
            } catch (Exception e) {
                LOG.warn("Kunne ikke sjekke {}", StringUtil.limit(bytes), e);
            }
        }
    }

}
