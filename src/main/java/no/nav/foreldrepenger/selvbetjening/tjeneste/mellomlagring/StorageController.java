package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static java.lang.String.format;
import static no.nav.foreldrepenger.selvbetjening.felles.Constants.ISSUER;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import no.nav.foreldrepenger.selvbetjening.felles.error.AttachmentsTooLargeException;
import no.nav.foreldrepenger.selvbetjening.tjeneste.TokenHandler;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
@RequestMapping("/rest/storage")
public class StorageController {

    private static final Logger log = getLogger(StorageController.class);
    private static final String KEY = "soknad";
    public static final int MAX_VEDLEGG_SIZE = 8 * 1024 * 1024;

    @Inject
    private TokenHandler tokenHandler;

    @Inject
    private Storage storage;

    @Inject
    private StorageCrypto crypto;

    @GetMapping
    public ResponseEntity<String> getSoknad() {
        String fnr = tokenHandler.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        log.trace("Retrieving søknad from directory " + directory);
        Optional<String> encryptedValue = storage.getTmp(directory, KEY);
        return encryptedValue
                .map(ev -> ResponseEntity.ok().body(crypto.decrypt(ev, fnr)))
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeSoknad(@RequestBody String soknad) {
        String fnr = tokenHandler.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        log.trace("Writing søknad to directory " + directory);
        String encryptedValue = crypto.encrypt(soknad, fnr);
        storage.putTmp(directory, KEY, encryptedValue);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSoknad() {
        String fnr = tokenHandler.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        log.trace("Deleting søknad from directory " + directory);
        storage.deleteTmp(directory, KEY);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "vedlegg/{key}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable("key") String key) {
        String fnr = tokenHandler.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        log.trace("Retrieving attachment from directory " + directory);
        return storage.getTmp(directory, key)
                .map(ev -> Attachment.fromJson(crypto.decrypt(ev, fnr)).asOKHTTPEntity())
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/vedlegg", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> storeAttachment(@RequestPart("vedlegg") MultipartFile attachmentMultipartFile) {
        Attachment attachment = Attachment.of(attachmentMultipartFile);
        if (attachment.size > MAX_VEDLEGG_SIZE) {
            throw new AttachmentsTooLargeException(format("Vedlegg-størrelse er %s, men kan ikke overstige %s",
                    byteCountToDisplaySize(attachment.size),
                    byteCountToDisplaySize(MAX_VEDLEGG_SIZE)));
        }

        String fnr = tokenHandler.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        log.trace("Writing attachment to directory " + directory);
        String encryptedValue = crypto.encrypt(attachment.toJson(), fnr);
        storage.putTmp(directory, attachment.uuid, encryptedValue);
        return ResponseEntity.created(attachment.uri()).build();
    }

    @DeleteMapping(path = "vedlegg/{key}")
    public ResponseEntity<String> deleteAttachment(@PathVariable("key") String key) {
        String fnr = tokenHandler.autentisertBruker();
        String directory = crypto.encryptDirectoryName(fnr);
        log.trace("Deleting attachment from directory " + directory);
        storage.deleteTmp(directory, key);
        return ResponseEntity.noContent().build();
    }

}
