package no.nav.foreldrepenger.selvbetjening.vedlegg;

import static no.nav.foreldrepenger.selvbetjening.util.StreamUtil.safeStream;
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
public class PDFEncryptionChecker implements VedleggSjekker {

    private static final Logger LOG = LoggerFactory.getLogger(PDFEncryptionChecker.class);

    @Override
    public void sjekk(Attachment... vedlegg) {
        safeStream(vedlegg).forEach(v -> check(v.bytes, v.filename));
    }

    @Override
    public void sjekk(Vedlegg... vedlegg) {
        safeStream(vedlegg).forEach(v -> check(v.getContent(), v.getBeskrivelse()));
    }

    private static void check(byte[] bytes, String name) {
        if (bytes != null && APPLICATION_PDF.equals(mediaType(bytes))) {
            try (var doc = PDDocument.load(bytes)) {
            } catch (InvalidPasswordException e) {
                throw new AttachmentPasswordProtectedException(name, e);
            } catch (Exception e) {
                LOG.warn("Kunne ikke sjekke {}", StringUtil.limit(bytes), e);
            }
        }
    }

}
