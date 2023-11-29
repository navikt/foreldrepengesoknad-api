package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;
import static no.nav.foreldrepenger.selvbetjening.mellomlagring.Ytelse.FORELDREPENGER;
import static no.nav.foreldrepenger.selvbetjening.mellomlagring.Ytelse.IKKE_OPPGITT;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.selvbetjening.http.ProtectedRestController;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;

@ProtectedRestController(MellomlagringController.REST_STORAGE)
public class MellomlagringController {
    private static final Logger LOG = getLogger(MellomlagringController.class);
    public static final String REST_STORAGE = "/rest/storage";

    private final KryptertMellomlagring mellomlagring;
    private final Image2PDFConverter converter;

    public MellomlagringController(KryptertMellomlagring mellomlagring, Image2PDFConverter converter) {
        this.mellomlagring = mellomlagring;
        this.converter = converter;
    }

    @Deprecated
    @GetMapping
    public ResponseEntity<String> lesSøknad() {
        var gammel = mellomlagring.lesKryptertSøknad(IKKE_OPPGITT);
        if (gammel.isPresent()) {
            LOG.info("Sletter mellomlagret søknad på gammelt endepunkt");
            lagreSøknadYtelse(FORELDREPENGER, gammel.get());
            slettSøknad(IKKE_OPPGITT);
            return ok().body(gammel.get());
        }
        return lesSøknad(FORELDREPENGER);
    }

    @Deprecated
    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void slettSøknad() {
        slettSøknad(IKKE_OPPGITT);
        slettSøknad(FORELDREPENGER);
    }

    @Deprecated
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void lagreSøknad(@RequestBody String soknad) {
        lagreSøknadYtelse(FORELDREPENGER, soknad);
    }

    @GetMapping(path = "/{ytelse}")
    public ResponseEntity<String> lesSøknad(@PathVariable("ytelse") @Valid Ytelse ytelse) {
        return mellomlagring.lesKryptertSøknad(ytelse)
            .map(s -> ok().body(s))
            .orElse(noContent().build());
    }

    @DeleteMapping(path = "/{ytelse}")
    @ResponseStatus(NO_CONTENT)
    public void slettSøknad(@PathVariable("ytelse") @Valid Ytelse ytelse) {
        mellomlagring.slettKryptertSøknad(ytelse);
    }

    @PostMapping(path = "/{ytelse}", consumes = APPLICATION_JSON_VALUE)
    public void lagreSøknadYtelse(@PathVariable("ytelse") @Valid Ytelse ytelse, @RequestBody String søknad) {
        mellomlagring.lagreKryptertSøknad(søknad, ytelse);
    }

    @GetMapping("/vedlegg/{key}")
    public ResponseEntity<byte[]> lesVedlegg(@PathVariable("key") @Pattern(regexp = FRITEKST) String key) {
        return mellomlagring.lesKryptertVedlegg(key)
                .map(this::found)
                .orElse(notFound().build());
    }

    @PostMapping(path = "/vedlegg", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> lagreVedlegg(@Valid @RequestPart("vedlegg") MultipartFile file) {
        var pdfBytes = converter.convert(getBytesNullSjekk(file));
        var attachment = Attachment.of(file.getOriginalFilename(), pdfBytes, MediaType.APPLICATION_PDF);
        mellomlagring.lagreKryptertVedlegg(attachment);
        return created(attachment.uri()).body(attachment.uuid);
    }

    @DeleteMapping("/vedlegg")
    @ResponseStatus(NO_CONTENT)
    public void slettVedlegg(@Valid @RequestBody @Size(min = 1, max = 100) List<@Pattern(regexp = FRITEKST) @NotEmpty String> uuid) {
        uuid.forEach(mellomlagring::slettKryptertVedlegg);
    }


    @DeleteMapping("/vedlegg/{key}")
    @ResponseStatus(NO_CONTENT)
    public void slettVedlegg(@PathVariable("key") @Pattern(regexp = FRITEKST) String key) {
        mellomlagring.slettKryptertVedlegg(key);
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

    private static byte[] getBytesNullSjekk(MultipartFile file) {
        var bytes = getBytesFraMultipart(file);
        if (bytes.length == 0) {
            throw new HttpServerErrorException(NOT_ACCEPTABLE, "Kan ikke mellomlagre vedlegg som ikke har innhold");
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
