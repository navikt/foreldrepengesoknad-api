package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggUtil.mediaType;
import static org.springframework.http.MediaType.APPLICATION_PDF;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.mellomlagring.Attachment;

@Component
public class PDFEncryptionVedleggSjekker implements VedleggSjekker {

    private static final Logger LOG = LoggerFactory.getLogger(PDFEncryptionVedleggSjekker.class);

    @Override
    public void sjekk(Attachment... vedlegg) {
        safeStream(vedlegg).forEach(v -> check(v.bytes()));
    }

    private static void check(byte[] bytes) {
        if (bytes != null && APPLICATION_PDF.equals(mediaType(bytes))) {
            try (var doc = Loader.loadPDF((bytes))) {
            } catch (InvalidPasswordException e) {
                LOG.info("Pdf feiler sjekk for kryptering", e);
                throw new AttachmentPasswordProtectedException();
            } catch (Exception e) {
                throw new AttachmentUnreadableException("Pdf er uleselig");
            }
        }
    }

}
