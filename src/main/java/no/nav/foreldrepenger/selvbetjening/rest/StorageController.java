package no.nav.foreldrepenger.selvbetjening.rest;

import no.nav.foreldrepenger.selvbetjening.crypto.Crypto;
import no.nav.foreldrepenger.selvbetjening.storage.Storage;
import no.nav.security.oidc.OIDCConstants;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@RequestMapping("/rest/storage")
public class StorageController {

    private static final Logger log = getLogger(StorageController.class);
    private static final String KEY_PREFIX = "fpsoknad-";

    private String encryptionPassphrase = "bogus";

    @Inject
    private OIDCRequestContextHolder contextHolder;

    @Inject
    private Storage storage;

    @GetMapping
    public ResponseEntity<String> retrieveSøknad() {
        String fnr = fnrFromOIDCToken();
        String encryptedKey = encrypt(KEY_PREFIX + fnr, fnr);
        log.info("Retrieving søknad with key " + encryptedKey + " from storage");
        String encryptedValue = storage.get(encryptedKey);
        return encryptedValue != null ?
                ResponseEntity.ok().body(decrypt(encryptedValue, fnr)) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeSøknad(@RequestBody String søknadJson) {
        String fnr = fnrFromOIDCToken();
        String encryptedKey = encrypt(KEY_PREFIX + fnr, fnr);
        log.info("Writing søknad with key " + encryptedKey + " to storage");
        String encryptedValue = encrypt(søknadJson, fnr);
        storage.put(encryptedKey, encryptedValue);
        return ResponseEntity.created(null).build();
    }

    private String fnrFromOIDCToken() {
        OIDCValidationContext context = (OIDCValidationContext) contextHolder
                .getRequestAttribute(OIDCConstants.OIDC_VALIDATION_CONTEXT);
        return context.getClaims("selvbetjening").getClaimSet().getSubject();
    }

    private String encrypt(String plaintext, String fnr) {
        Crypto crypto = new Crypto(encryptionPassphrase, fnr);
        return crypto.encrypt(plaintext);
    }

    private String decrypt(String encrypted, String fnr) {
        Crypto crypto = new Crypto(encryptionPassphrase, fnr);
        return crypto.decrypt(encrypted);
    }

}
