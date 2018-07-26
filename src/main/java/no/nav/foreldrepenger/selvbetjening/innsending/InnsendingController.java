package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.util.Arrays.stream;
import static no.nav.foreldrepenger.selvbetjening.innsending.InnsendingController.REST_ENGANGSSTONAD;
import static no.nav.foreldrepenger.selvbetjening.innsending.InnsendingController.REST_SOKNAD;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.util.List;

import javax.inject.Inject;

import no.nav.foreldrepenger.selvbetjening.felles.storage.Storage;
import no.nav.foreldrepenger.selvbetjening.felles.storage.StorageCrypto;
import no.nav.foreldrepenger.selvbetjening.felles.util.FnrExtractor;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentsTooLargeException;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.Innsending;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@RequestMapping({ REST_SOKNAD, REST_ENGANGSSTONAD })
public class InnsendingController {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingController.class);

    public static final String REST_ENGANGSSTONAD = "/rest/engangsstonad"; // TODO: Fjern denne når frontend er
                                                                           // oppdatert
    public static final String REST_SOKNAD = "/rest/soknad";

    private static final double MB = 1024 * 1024;
    private static final double MAX_VEDLEGG_SIZE = 7.5 * MB;

    private final Innsending innsending;

    @Inject
    public RestTemplate http;

    @Inject
    private OIDCRequestContextHolder contextHolder;

    @Inject
    private Storage storage;

    @Inject
    private StorageCrypto crypto;

    @Inject
    public InnsendingController(Innsending innsending) {
        this.innsending = innsending;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Kvittering> sendInn(@RequestBody Søknad søknad) {
        LOG.info("Mottok søknad  {}", søknad);

        søknad.vedlegg.stream().forEach(this::fetchAttachment);
        checkVedleggTooLarge(søknad.vedlegg);
        ResponseEntity<Kvittering> respons = innsending.sendInn(søknad);

        deleteFromTempStorage(FnrExtractor.extract(contextHolder), søknad);

        return respons;
    }

    // TODO: Fjern denne når frontend er oppdatert
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Kvittering> sendInnWithMultipart(@RequestPart("soknad") Søknad søknad,
            @RequestPart("vedlegg") MultipartFile... vedlegg) throws Exception {
        LOG.info("Mottok søknad (multipart) {}", søknad);
        checkVedleggTooLargeMultipart(vedlegg);
        return innsending.sendInn(søknad, vedlegg);
    }

    // TODO: Fjern denne når frontend er oppdatert
    private void checkVedleggTooLargeMultipart(MultipartFile... vedlegg) {
        long total = stream(vedlegg)
                .mapToLong(MultipartFile::getSize)
                .sum();
        if (total > MAX_VEDLEGG_SIZE) {
            throw new AttachmentsTooLargeException("Samlet filstørrelse for alle vedlegg er " + total
                    + ", men kan ikke overstige " + MAX_VEDLEGG_SIZE + " bytes");
        }
    }

    private void checkVedleggTooLarge(List<Vedlegg> vedlegg) {
        long total = vedlegg.stream()
                .mapToLong(v -> v.content.length)
                .sum();
        if (total > MAX_VEDLEGG_SIZE) {
            throw new AttachmentsTooLargeException("Samlet filstørrelse for alle vedlegg er " + total
                    + ", men kan ikke overstige " + MAX_VEDLEGG_SIZE + " bytes");
        }
    }

    private void fetchAttachment(Vedlegg vedlegg) {
        vedlegg.content = http.getForObject(vedlegg.url, byte[].class);
    }

    private void fetchAndDeleteAttachment(Vedlegg vedlegg) {
        vedlegg.content = http.getForObject(vedlegg.url, byte[].class);
        http.delete(vedlegg.url);
    }

    private void deleteFromTempStorage(String fnr, Søknad søknad) {
        søknad.vedlegg.stream().forEach(this::fetchAndDeleteAttachment);
        storage.delete(crypto.encryptDirectoryName(fnr), "soknad");
    }
}
