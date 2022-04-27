package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;

@Validated
@ProtectedRestController(MellomlagringController.REST_STORAGE)
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

    @GetMapping("/vedlegg/{key}")
    public ResponseEntity<byte[]> lesVedlegg(@PathVariable("key") @Pattern(regexp = FRITEKST) String key) {
        return mellomlagring.lesKryptertVedlegg(key)
                .map(this::found)
                .orElse(notFound().build());
    }

    @PostMapping(path = "/vedlegg", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> lagreVedlegg(@Valid @RequestPart("vedlegg") MultipartFile attachmentMultipartFile) {
        Attachment attachment = Attachment.of(attachmentMultipartFile);
        mellomlagring.lagreKryptertVedlegg(attachment);
        return created(attachment.uri()).body(attachment.uuid);
    }

    @DeleteMapping("/vedlegg/{key}")
    @ResponseStatus(NO_CONTENT)
    public void slettVedlegg(@PathVariable("key") @Pattern(regexp = FRITEKST) String key) {
        mellomlagring.slettKryptertVedlegg(key);
    }

    @GetMapping("/kvittering/{type}")
    public ResponseEntity<String> lesKvittering(@PathVariable("type") @Pattern(regexp = FRITEKST) String type) {
        return noContent().build();
    }

    @PostMapping(value = "/kvittering/{type}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void lagreKvittering(@PathVariable("type") @Pattern(regexp = FRITEKST) String type,
                                @RequestBody @Pattern(regexp = FRITEKST) String kvittering) {
        // gjør ingenting, kan fjernes når frontend har fjernet kall
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
