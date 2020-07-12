package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;

@ProtectedRestController
@RequestMapping(MellomlagringController.REST_STORAGE)
public class MellomlagringController {

    public static final String REST_STORAGE = "/rest/storage";

    private final KryptertMellomlagring mellomlagring;

    public MellomlagringController(KryptertMellomlagring mellomlagring) {
        this.mellomlagring = mellomlagring;
    }

    @GetMapping
    public ResponseEntity<String> lesSøknad() {
        return mellomlagring.lesKryptertSøknad()
                .map(s -> ok().body(s))
                .orElse(noContent().build());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void lagreSøknad(@RequestBody String soknad) {
        mellomlagring.lagreKryptertSøknad(soknad);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void slettSøknad() {
        mellomlagring.slettKryptertSøknad();
    }

    @GetMapping("vedlegg/{key}")
    public ResponseEntity<byte[]> lesVedlegg(@PathVariable("key") String key) {
        return mellomlagring.lesKryptertVedlegg(key)
                .map(this::found)
                .orElse(notFound().build());
    }

    @PostMapping(path = "/vedlegg", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> lagreVedlegg(@RequestPart("vedlegg") MultipartFile attachmentMultipartFile) {
        Attachment attachment = Attachment.of(attachmentMultipartFile);
        mellomlagring.lagreKryptertVedlegg(attachment);
        return created(attachment.uri()).body(attachment.uuid);
    }

    @DeleteMapping("vedlegg/{key}")
    @ResponseStatus(NO_CONTENT)
    public void slettVedlegg(@PathVariable("key") String key) {
        mellomlagring.slettKryptertVedlegg(key);
    }

    @GetMapping("kvittering/{type}")
    public ResponseEntity<String> lesKvittering(@PathVariable("type") String type) {
        return mellomlagring.lesKryptertKvittering(type)
                .map(kvittering -> ok().body(kvittering))
                .orElse(noContent().build());
    }

    @PostMapping(value = "kvittering/{type}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void lagreKvittering(@PathVariable("type") String type, @RequestBody String kvittering) {
        mellomlagring.lagreKryptertKvittering(type, kvittering);
    }

    private ResponseEntity<byte[]> found(Attachment att) {
        return ok()
                .contentType(att.getContentType())
                .contentLength(att.getSize().toBytes())
                .body(att.getBytes());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [mellomlagringTjeneste=" + mellomlagring + "]";
    }

}
