package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static java.lang.String.format;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.InnsendingController.REST_SOKNAD;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Attachment.MAX_TOTAL_VEDLEGG_SIZE;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.CONFIDENTIAL;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentsTooLargeException;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.StorageService;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
@RequestMapping(path = REST_SOKNAD, produces = APPLICATION_JSON_VALUE)
public class InnsendingController {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingController.class);

    public static final String REST_SOKNAD = "/rest/soknad";

    private final Innsending innsending;
    private final StorageService storageService;

    public InnsendingController(Innsending innsending, StorageService storageService) {
        this.innsending = innsending;
        this.storageService = storageService;
    }

    @PostMapping
    public Kvittering sendInn(@RequestBody Søknad søknad) {
        LOG.info("Mottok søknad");
        LOG.info(CONFIDENTIAL, "{}", søknad);
        søknad.vedlegg.forEach(this::hentVedlegg);
        sjekkSamletStørrelseVedlegg(søknad.vedlegg);
        Kvittering respons = innsending.sendInn(søknad);
        slettMellomlagring(søknad);
        return respons;
    }

    @PostMapping("/ettersend")
    public Kvittering sendInn(@RequestBody Ettersending ettersending) {
        LOG.info("Mottok ettersending");
        LOG.info(CONFIDENTIAL, "{}", ettersending);
        ettersending.vedlegg.forEach(this::hentVedlegg);
        sjekkSamletStørrelseVedlegg(ettersending.vedlegg);
        Kvittering respons = innsending.sendInn(ettersending);
        ettersending.vedlegg.forEach(this::slettVedlegg);
        return respons;
    }

    @PostMapping("/endre")
    public Kvittering endre(@RequestBody Søknad søknad) {
        LOG.info(CONFIDENTIAL, "Mottok endringssøknad");
        LOG.info(CONFIDENTIAL, "{}", søknad);
        søknad.vedlegg.forEach(this::hentVedlegg);
        sjekkSamletStørrelseVedlegg(søknad.vedlegg);
        Kvittering respons = innsending.endre(søknad);
        slettMellomlagring(søknad);
        return respons;
    }

    private static void sjekkSamletStørrelseVedlegg(List<Vedlegg> vedlegg) {
        long total = vedlegg.stream()
                .filter(v -> v.content != null)
                .mapToLong(v -> v.content.length)
                .sum();

        if (total > MAX_TOTAL_VEDLEGG_SIZE) {
            throw new AttachmentsTooLargeException(
                    format("Samlet filstørrelse for alle vedlegg er %s, men må være mindre enn %s", mb(total),
                            mb(MAX_TOTAL_VEDLEGG_SIZE)));
        }
    }

    private void hentVedlegg(Vedlegg vedlegg) {
        if (vedlegg.url != null) {
            vedlegg.content = storageService.hentVedlegg(vedlegg.uuid)
                    .map(a -> a.bytes)
                    .orElse(new byte[] {});
        }
    }

    private void slettMellomlagring(Søknad søknad) {
        LOG.info("Sletter mellomlagret søknad og vedlegg");
        søknad.vedlegg.forEach(this::slettVedlegg);
        storageService.slettSøknad();
    }

    private void slettVedlegg(Vedlegg vedlegg) {
        if (vedlegg.url != null) {
            storageService.slettVedlegg(vedlegg.uuid);
        }
    }

    private static String mb(long byteCount) {
        return byteCountToDisplaySize(byteCount);
    }
}