package no.nav.foreldrepenger.selvbetjening.felles;

import no.nav.foreldrepenger.selvbetjening.felles.crypto.Crypto;
import no.nav.foreldrepenger.selvbetjening.felles.storage.Storage;
import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@RequestMapping("/rest/storage")
public class StorageController {

    private static final Logger log = getLogger(StorageController.class);
    private static final String KEY = "soknad";

    @Value("${FORELDREPENGESOKNAD_API_STORAGE_PASSWORD}")
    private String encryptionPassphrase;

    @Inject
    private OIDCRequestContextHolder contextHolder;

    @Inject
    private Storage storage;

    @GetMapping
    public ResponseEntity<String> retrieveSøknad() {
        String fnr = fnrFromOIDCToken();
        String directory = encrypt(fnr, fnr);
        log.info("Retrieving søknad from directory " + directory);
        Optional<String> encryptedValue = storage.get(directory, KEY);
        return encryptedValue
                .map(ev -> ResponseEntity.ok().body(decrypt(ev, fnr)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeSøknad(@RequestBody String søknadJson) {
        String fnr = fnrFromOIDCToken();
        String directory = encrypt(fnr, fnr);
        log.info("Writing søknad to directory " + directory);
        String encryptedValue = encrypt(søknadJson, fnr);
        storage.put(directory, KEY, encryptedValue);
        return ResponseEntity.created(null).build();
    }

    private String fnrFromOIDCToken() {
        OIDCValidationContext context = (OIDCValidationContext) contextHolder
                .getRequestAttribute(OIDCConstants.OIDC_VALIDATION_CONTEXT);
        return context.getClaims("selvbetjening").getClaimSet().getSubject();
    }

    private String encrypt(String plaintext, String salt) {
        Crypto crypto = new Crypto(encryptionPassphrase, salt);
        return crypto.encrypt(plaintext);
    }

    private String decrypt(String encrypted, String salt) {
        Crypto crypto = new Crypto(encryptionPassphrase, salt);
        return crypto.decrypt(encrypted);
    }

}
