package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentsTooLargeException;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.exceptions.OIDCTokenValidatorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.lang.String.format;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.*;

@RestController
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
@RequestMapping(StorageController.REST_STORAGE)
public class StorageController {

    public static final String REST_STORAGE = "/rest/storage";
    public static final int MAX_VEDLEGG_SIZE = 8 * 1024 * 1024;

    private final StorageService service;

    public StorageController(StorageService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<String> getSoknad() throws OIDCTokenValidatorException {
        Optional<String> muligSøknad = service.hentSøknad();
        return muligSøknad
                .map(s -> ok().body(s))
                .orElse(noContent().build());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeSoknad(@RequestBody String soknad) throws OIDCTokenValidatorException {
        service.lagreSøknad(soknad);
        return noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSoknad() throws OIDCTokenValidatorException {
        service.slettSøknad();
        return noContent().build();
    }

    @GetMapping("vedlegg/{key}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable("key") String key) throws OIDCTokenValidatorException {
        Optional<Attachment> muligVedlegg = service.hentVedlegg(key);
        return muligVedlegg
                .map(Attachment::asOKHTTPEntity)
                .orElse(notFound().build());
    }

    @PostMapping(path = "/vedlegg", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> storeAttachment(@RequestPart("vedlegg") MultipartFile attachmentMultipartFile) throws OIDCTokenValidatorException {
        Attachment attachment = Attachment.of(attachmentMultipartFile);
        if (attachment.size > MAX_VEDLEGG_SIZE) {
            throw new AttachmentsTooLargeException(tooLargeErrorMessage(attachment.size));
        }

        service.lagreVedlegg(attachment);
        return created(attachment.uri()).body(attachment.uuid);
    }

    @DeleteMapping("vedlegg/{key}")
    public ResponseEntity<String> deleteAttachment(@PathVariable("key") String key) throws OIDCTokenValidatorException {
        service.slettVedlegg(key);
        return noContent().build();
    }

    @GetMapping("kvittering/{type}")
    public ResponseEntity<String> getKvittering(@PathVariable("type") String type) {
        Optional<String> muligKvittering = service.hentKvittering(type);
        return muligKvittering
                .map(kvittering -> ok().body(kvittering))
                .orElse(noContent().build());
    }

    @PostMapping(value = "kvittering/{type}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeKvittering(@PathVariable("type") String type, @RequestBody String kvittering) {
        service.lagreKvittering(type, kvittering);
        return noContent().build();
    }


    private String tooLargeErrorMessage(long attachmentSize) {
        return format("Vedlegg-størrelse er %s, men kan ikke overstige %s",
                byteCountToDisplaySize(attachmentSize),
                byteCountToDisplaySize(MAX_VEDLEGG_SIZE));
    }

}
