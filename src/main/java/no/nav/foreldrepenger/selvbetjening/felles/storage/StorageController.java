package no.nav.foreldrepenger.selvbetjening.felles.storage;

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

import no.nav.foreldrepenger.selvbetjening.felles.util.FnrExtractor;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.context.OIDCRequestContextHolder;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
@RequestMapping("/rest/storage")
public class StorageController {

    private static final Logger log = getLogger(StorageController.class);
    private static final String KEY = "soknad";

    @Inject
    private OIDCRequestContextHolder contextHolder;

    @Inject
    private Storage storage;

    @Inject
    private StorageCrypto crypto;

    @GetMapping
    public ResponseEntity<String> getSoknad() {
        String fnr = FnrExtractor.extract(contextHolder);
        String directory = crypto.encryptDirectoryName(fnr);
        log.info("Retrieving søknad from directory " + directory);
        Optional<String> encryptedValue = storage.getTmp(directory, KEY);
        return encryptedValue
                .map(ev -> ResponseEntity.ok().body(crypto.decrypt(ev, fnr)))
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeSoknad(@RequestBody String soknad) {
        String fnr = FnrExtractor.extract(contextHolder);
        String directory = crypto.encryptDirectoryName(fnr);
        log.info("Writing søknad to directory " + directory);
        String encryptedValue = crypto.encrypt(soknad, fnr);
        storage.putTmp(directory, KEY, encryptedValue);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSoknad() {
        String fnr = FnrExtractor.extract(contextHolder);
        String directory = crypto.encryptDirectoryName(fnr);
        log.info("Deleting søknad from directory " + directory);
        storage.deleteTmp(directory, KEY);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "vedlegg/{key}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable("key") String key) {
        String fnr = FnrExtractor.extract(contextHolder);
        String directory = crypto.encryptDirectoryName(fnr);
        log.info("Retrieving attachment from directory " + directory);
        return storage.getTmp(directory, key)
                .map(ev -> Attachment.fromJson(crypto.decrypt(ev, fnr)).asOKHTTPEntity())
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/vedlegg", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> storeAttachment(@RequestPart("vedlegg") MultipartFile attachmentMultipartFile) {
        Attachment attachment = Attachment.of(attachmentMultipartFile);
        String fnr = FnrExtractor.extract(contextHolder);
        String directory = crypto.encryptDirectoryName(fnr);
        log.info("Writing attachment to directory " + directory);
        String encryptedValue = crypto.encrypt(attachment.toJson(), fnr);
        storage.putTmp(directory, attachment.uuid, encryptedValue);
        return ResponseEntity.created(attachment.uri()).build();
    }


    @DeleteMapping(path = "vedlegg/{key}")
    public ResponseEntity<String> deleteAttachment(@PathVariable("key") String key) {
        String fnr = FnrExtractor.extract(contextHolder);
        String directory = crypto.encryptDirectoryName(fnr);
        log.info("Deleting attachment from directory " + directory);
        storage.deleteTmp(directory, key);
        return ResponseEntity.noContent().build();
    }

}
