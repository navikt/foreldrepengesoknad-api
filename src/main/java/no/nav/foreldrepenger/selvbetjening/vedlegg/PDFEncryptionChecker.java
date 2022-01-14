package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static no.nav.foreldrepenger.common.util.StringUtil.limit;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggUtil.mediaType;
import static org.springframework.http.MediaType.APPLICATION_PDF;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggFrontend;
import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;

@Component
public class PDFEncryptionChecker {

    private static final Logger LOG = LoggerFactory.getLogger(PDFEncryptionChecker.class);

    public void checkEncrypted(Attachment v) {
        check(v.bytes);
    }

    public void sjekkKryptert(VedleggFrontend v) {
        check(v.getContent());
    }

    private static void check(byte[] bytes) {
        if (bytes != null && APPLICATION_PDF.equals(mediaType(bytes))) {
            try (var doc = PDDocument.load(bytes)) {
            } catch (InvalidPasswordException e) {
                throw new AttachmentPasswordProtectedException(e);
            } catch (Exception e) {
                LOG.warn("Kunne ikke sjekke {}", limit(bytes), e);
            }
        }
    }

}
