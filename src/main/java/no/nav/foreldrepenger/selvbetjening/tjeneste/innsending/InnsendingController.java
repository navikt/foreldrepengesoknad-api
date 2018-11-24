package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static java.lang.String.format;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.InnsendingController.REST_SOKNAD;
import static no.nav.foreldrepenger.selvbetjening.util.Constants.ISSUER;
import static no.nav.foreldrepenger.selvbetjening.util.EnvUtil.CONFIDENTIAL;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.selvbetjening.error.AttachmentsTooLargeException;
import no.nav.foreldrepenger.selvbetjening.tjeneste.TokenHandler;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Kvittering;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.StorageCrypto;
import no.nav.foreldrepenger.selvbetjening.util.Enabled;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
@RequestMapping(REST_SOKNAD)
public class InnsendingController {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingController.class);

    public static final String REST_SOKNAD = "/rest/soknad";

    private static final long MB = 1024 * 1024;
    private static final long MAX_VEDLEGG_SIZE = 8 * MB;

    private final Innsending innsending;

    @Inject
    public RestTemplate http;

    @Inject
    private TokenHandler tokenHandler;

    @Inject
    private Storage storage;

    @Inject
    private StorageCrypto crypto;

    @Inject
    public InnsendingController(Innsending innsending) {
        this.innsending = innsending;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public Kvittering sendInn(@RequestBody Søknad søknad) {
        LOG.info(CONFIDENTIAL, "Mottok søknad: {}", søknad);
        søknad.vedlegg.forEach(this::fetchAttachment);
        checkVedleggTooLarge(søknad.vedlegg);
        Kvittering respons = innsending.sendInn(søknad);
        deleteFromTempStorage(tokenHandler.autentisertBruker(), søknad);
        return respons;
    }

    @PostMapping(path = "/ettersend", consumes = APPLICATION_JSON_VALUE)
    public Kvittering sendInn(@RequestBody Ettersending ettersending) {
        LOG.info(CONFIDENTIAL, "Mottok ettersending: {}", ettersending);
        ettersending.vedlegg.forEach(this::fetchAttachment);
        checkVedleggTooLarge(ettersending.vedlegg);
        Kvittering respons = innsending.sendInn(ettersending);
        ettersending.vedlegg.forEach(this::fetchAndDeleteAttachment);
        return respons;
    }

    @PostMapping(path = "/endre", consumes = APPLICATION_JSON_VALUE)
    public Kvittering endre(@RequestBody Søknad søknad) {
        if (!Enabled.ENDRINGSSØKNAD) {
            LOG.info("Mottok endringssøknad, men den er togglet av!");
            throw new BadRequestException("Endringssøknad is not supported yet");
        }

        LOG.info(CONFIDENTIAL, "Mottok endringssøknad: {}", søknad);
        søknad.vedlegg.forEach(this::fetchAttachment);
        checkVedleggTooLarge(søknad.vedlegg);
        Kvittering respons = innsending.endre(søknad);
        søknad.vedlegg.forEach(this::fetchAndDeleteAttachment);
        return respons;
    }

    private void checkVedleggTooLarge(List<Vedlegg> vedlegg) {
        long total = vedlegg.stream()
                .filter(v -> v.content != null)
                .mapToLong(v -> v.content.length)
                .sum();
        if (total > MAX_VEDLEGG_SIZE) {
            throw new AttachmentsTooLargeException(
                    format("Samlet filstørrelse for alle vedlegg er %s, men kan ikke overstige %s",
                            byteCountToDisplaySize(total),
                            byteCountToDisplaySize(MAX_VEDLEGG_SIZE)));
        }
    }

    private void fetchAttachment(Vedlegg vedlegg) {
        if (vedlegg.url != null) {
            vedlegg.content = http.getForObject(vedlegg.url, byte[].class);
        }
    }

    private void fetchAndDeleteAttachment(Vedlegg vedlegg) {
        if (vedlegg.url != null) {
            vedlegg.content = http.getForObject(vedlegg.url, byte[].class);
            http.delete(vedlegg.url);
        }
    }

    private void deleteFromTempStorage(String fnr, Søknad søknad) {
        søknad.vedlegg.forEach(this::fetchAndDeleteAttachment);
        storage.delete(crypto.encryptDirectoryName(fnr), "soknad");
    }
}