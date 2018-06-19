package no.nav.foreldrepenger.selvbetjening.innsending;

import no.nav.foreldrepenger.selvbetjening.felles.attachments.exceptions.AttachmentsTooLargeException;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Kvittering;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.Innsending;
import no.nav.security.spring.oidc.validation.api.ProtectedWithClaims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

import java.util.List;

import static java.util.Arrays.stream;
import static no.nav.foreldrepenger.selvbetjening.innsending.InnsendingController.REST_ENGANGSSTONAD;
import static no.nav.foreldrepenger.selvbetjening.innsending.InnsendingController.REST_SOKNAD;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = {"acr=Level4"})
@RequestMapping({REST_SOKNAD, REST_ENGANGSSTONAD})
public class InnsendingController {

    public static final String REST_ENGANGSSTONAD = "/rest/engangsstonad"; // TODO: Fjern denne når frontend er oppdatert
    public static final String REST_SOKNAD = "/rest/soknad";

    private static final double MB = 1024 * 1024;
    private static final double MAX_VEDLEGG_SIZE = 7.5 * MB;

    private final Innsending innsending;

    @Inject
    public RestTemplate http;

    @Inject
    public InnsendingController(Innsending innsending) {
        this.innsending = innsending;
    }


    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Kvittering> sendInn(@RequestBody Søknad søknad) throws Exception{
        søknad.vedlegg.stream().forEach(this::fetchAndDeleteAttachment);
        checkVedleggTooLarge(søknad.vedlegg);
        return innsending.sendInn(søknad);
    }

    private void checkVedleggTooLarge(List<Vedlegg> vedlegg) {
        long total = vedlegg.stream()
                .mapToLong(v -> v.content.length)
                .sum();
        if (total > MAX_VEDLEGG_SIZE) {
            throw new AttachmentsTooLargeException("Samlet filstørrelse for alle vedlegg er " + total + ", men kan ikke overstige " + MAX_VEDLEGG_SIZE + " bytes");
        }
    }

    private void fetchAndDeleteAttachment(Vedlegg vedlegg) {
        vedlegg.content = http.getForObject(vedlegg.url, byte[].class);
        http.delete(vedlegg.url);
    }
}
