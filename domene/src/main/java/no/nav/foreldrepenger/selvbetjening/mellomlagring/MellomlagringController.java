package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.DelegerendeVedleggSjekker.DELEGERENDE;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;
import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@ProtectedRestController(MellomlagringController.REST_STORAGE)
public class MellomlagringController {
    public static final String REST_STORAGE = "/rest/storage";

    private final KryptertMellomlagring mellomlagring;
    private final Image2PDFConverter converter;
    private final VedleggSjekker sjekker;

    public MellomlagringController(KryptertMellomlagring mellomlagring,
                                   Image2PDFConverter converter,
                                   @Qualifier(DELEGERENDE) VedleggSjekker sjekker) {
        this.mellomlagring = mellomlagring;
        this.converter = converter;
        this.sjekker = sjekker;
    }

    @GetMapping(path = "/aktive")
    public ResponseEntity<AktivMellomlagringDto> finnesDetAktivMellomlagring() {
        return ok().body(mellomlagring.finnesAktivMellomlagring());
    }

    @GetMapping(path = "/{ytelse}")
    public ResponseEntity<String> lesSøknad(@PathVariable("ytelse") @Valid YtelseMellomlagringType ytelse) {
        return mellomlagring.lesKryptertSøknad(ytelse).map(s -> ok().body(s)).orElse(noContent().build());
    }

    @DeleteMapping(path = "/{ytelse}")
    @ResponseStatus(NO_CONTENT)
    public void slettMellomlagring(@PathVariable("ytelse") @Valid YtelseMellomlagringType ytelse) {
        mellomlagring.slettMellomlagring(ytelse);
    }

    @PostMapping(path = "/{ytelse}", consumes = APPLICATION_JSON_VALUE)
    public void lagreSøknadYtelse(@PathVariable("ytelse") @Valid YtelseMellomlagringType ytelse, @RequestBody String søknad) {
        mellomlagring.lagreKryptertSøknad(søknad, ytelse);
    }

    @GetMapping("/{ytelse}/vedlegg/{key}")
    public ResponseEntity<byte[]> lesVedlegg(@PathVariable("ytelse") @Valid YtelseMellomlagringType ytelse,
                                             @PathVariable("key") @Pattern(regexp = FRITEKST) String key) {
        return mellomlagring.lesKryptertVedlegg(key, ytelse).map(this::found).orElse(notFound().build());
    }

    @PostMapping(path = "/{ytelse}/vedlegg", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> lagreVedlegg(@RequestPart("vedlegg") @Valid MultipartFile file,
                                               @PathVariable("ytelse") @Valid YtelseMellomlagringType ytelse,
                                               // Kan spesifisere uuid hvis ønskelig
                                               @RequestParam(value = "uuid", required = false) @Valid UUID uuid) {
        var originalBytes = getBytesNullSjekk(file);
        sjekker.sjekk(Attachment.of(originalBytes));

        var pdfBytes = converter.convert(originalBytes);

        var attachment = uuid != null ? new Attachment(pdfBytes, uuid.toString()) : Attachment.of(pdfBytes);
        mellomlagring.lagreKryptertVedlegg(attachment, ytelse);
        return created(attachment.uri()).body(attachment.uuid());
    }

    @DeleteMapping("/{ytelse}/vedlegg/{key}")
    @ResponseStatus(NO_CONTENT)
    public void slettVedlegg(@PathVariable("ytelse") @Valid YtelseMellomlagringType ytelse,
                             @PathVariable("key") @Pattern(regexp = FRITEKST) String key) {
        mellomlagring.slettKryptertVedlegg(key, ytelse);
    }

    private ResponseEntity<byte[]> found(byte[] innhold) {
        return ok().contentType(MediaType.APPLICATION_PDF).contentLength(innhold.length).body(innhold);
    }

    private static byte[] getBytesNullSjekk(MultipartFile file) {
        var bytes = getBytesFraMultipart(file);
        if (bytes.length == 0) {
            throw new AttachmentEmptyException("Kan ikke mellomlagre vedlegg som ikke har innhold");
        }
        return bytes;
    }

    private static byte[] getBytesFraMultipart(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new IllegalStateException("Klarte ikke hente innhold for mellomlagret vedlegg", e);
        }
    }
}
