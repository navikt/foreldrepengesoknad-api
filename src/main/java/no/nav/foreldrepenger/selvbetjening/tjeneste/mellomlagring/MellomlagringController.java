package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.util.Optional;

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

import no.nav.security.token.support.core.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
@RequestMapping(MellomlagringController.REST_STORAGE)
public class MellomlagringController {

    public static final String REST_STORAGE = "/rest/storage";

    private final MellomlagringTjeneste mellomlagringTjeneste;

    public MellomlagringController(MellomlagringTjeneste mellomlagringTjeneste) {
        this.mellomlagringTjeneste = mellomlagringTjeneste;
    }

    @GetMapping
    public ResponseEntity<String> getSoknad() {
        Optional<String> muligSøknad = mellomlagringTjeneste.hentSøknad();
        return muligSøknad
                .map(s -> ok().body(s))
                .orElse(noContent().build());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeSoknad(@RequestBody String soknad) {
        mellomlagringTjeneste.lagreSøknad(soknad);
        return noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSoknad() {
        mellomlagringTjeneste.slettSøknad();
        return noContent().build();
    }

    @GetMapping("vedlegg/{key}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable("key") String key) {
        Optional<Attachment> muligVedlegg = mellomlagringTjeneste.hentVedlegg(key);
        return muligVedlegg
                .map(Attachment::asOKHTTPEntity)
                .orElse(notFound().build());
    }

    @PostMapping(path = "/vedlegg", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> storeAttachment(@RequestPart("vedlegg") MultipartFile attachmentMultipartFile) {
        Attachment attachment = Attachment.of(attachmentMultipartFile);
        mellomlagringTjeneste.lagreVedlegg(attachment);
        return created(attachment.uri()).body(attachment.uuid);
    }

    @DeleteMapping("vedlegg/{key}")
    public ResponseEntity<String> deleteAttachment(@PathVariable("key") String key) {
        mellomlagringTjeneste.slettVedlegg(key);
        return noContent().build();
    }

    @GetMapping("kvittering/{type}")
    public ResponseEntity<String> getKvittering(@PathVariable("type") String type) {
        Optional<String> muligKvittering = mellomlagringTjeneste.hentKvittering(type);
        return muligKvittering
                .map(kvittering -> ok().body(kvittering))
                .orElse(noContent().build());
    }

    @PostMapping(value = "kvittering/{type}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeKvittering(@PathVariable("type") String type, @RequestBody String kvittering) {
        mellomlagringTjeneste.lagreKvittering(type, kvittering);
        return noContent().build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [mellomlagringTjeneste=" + mellomlagringTjeneste + "]";
    }

}
