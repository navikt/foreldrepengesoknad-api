package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import static java.lang.String.format;
import static no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.InnsendingController.REST_SOKNAD;
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
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.StorageCrypto;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;
import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.exceptions.OIDCTokenValidatorException;

@RestController
@ProtectedWithClaims(issuer = ISSUER, claimMap = { "acr=Level4" })
@RequestMapping(path = REST_SOKNAD, produces = APPLICATION_JSON_VALUE)
public class InnsendingController {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingController.class);

    public static final String REST_SOKNAD = "/rest/soknad";

    private static final long MB = 1024 * 1024;
    private static final long MAX_VEDLEGG_SIZE = 32 * MB;
    private static final long MAX_VEDLEGG_SIZE_DOKMOT = 8 * MB;

    private final Innsending innsending;

    private final VedleggTjeneste vedleggTjeneste;

    private final TokenUtil tokenHandler;

    private final Storage storage;

    private final StorageCrypto crypto;

    public InnsendingController(Innsending innsending, VedleggTjeneste vedleggTjeneste, TokenUtil tokenHandler,
            Storage storage,
            StorageCrypto crypto) {
        this.innsending = innsending;
        this.vedleggTjeneste = vedleggTjeneste;
        this.tokenHandler = tokenHandler;
        this.storage = storage;
        this.crypto = crypto;
    }

    @PostMapping
    public Kvittering sendInn(@RequestBody Søknad søknad) throws OIDCTokenValidatorException {
        LOG.info(CONFIDENTIAL, "Mottok søknad: {}", søknad);
        søknad.vedlegg.forEach(this::fetchAttachment);
        checkVedleggTooLarge(søknad.vedlegg, søknad.type);
        Kvittering respons = innsending.sendInn(søknad);
        deleteFromTempStorage(tokenHandler.autentisertBruker(), søknad);
        return respons;
    }

    @PostMapping("/ettersend")
    public Kvittering sendInn(@RequestBody Ettersending ettersending) {
        LOG.info(CONFIDENTIAL, "Mottok ettersending: {}", ettersending);
        ettersending.vedlegg.forEach(this::fetchAttachment);
        checkVedleggTooLarge(ettersending.vedlegg, "ettersending");
        Kvittering respons = innsending.sendInn(ettersending);
        ettersending.vedlegg.forEach(this::deleteAttachment);
        return respons;
    }

    @PostMapping("/endre")
    public Kvittering endre(@RequestBody Søknad søknad) {
        LOG.info(CONFIDENTIAL, "Mottok endringssøknad: {}", søknad);
        søknad.vedlegg.forEach(this::fetchAttachment);
        checkVedleggTooLarge(søknad.vedlegg, søknad.type);
        Kvittering respons = innsending.endre(søknad);
        deleteFromTempStorage(tokenHandler.autentisertBruker(), søknad);
        return respons;
    }

    private static void checkVedleggTooLarge(List<Vedlegg> vedlegg, String type) {
        long total = vedlegg.stream()
                .filter(v -> v.content != null)
                .mapToLong(v -> v.content.length)
                .sum();

        long max = type.equals("engangsstønad") ? MAX_VEDLEGG_SIZE_DOKMOT : MAX_VEDLEGG_SIZE;

        if (total > max) {
            throw new AttachmentsTooLargeException(format(
                    "Samlet filstørrelse for alle vedlegg er %s, men må være mindre enn %s", mb(total), mb(max)));
        }
    }

    private void fetchAttachment(Vedlegg vedlegg) {
        if (vedlegg.url != null) {
            vedlegg.content = vedleggTjeneste.hentVedlegg(vedlegg.url);
        }
    }

    private void deleteAttachment(Vedlegg vedlegg) {
        if (vedlegg.url != null) {
            vedleggTjeneste.slettVedlegg(vedlegg.url);
        }
    }

    private void deleteFromTempStorage(String fnr, Søknad søknad) {
        LOG.info("Sletter mellomlagret søknad og vedlegg");
        søknad.vedlegg.forEach(this::deleteAttachment);
        storage.deleteTmp(crypto.encryptDirectoryName(fnr), "soknad");
    }

    private static String mb(long byteCount) {
        return byteCountToDisplaySize(byteCount);
    }
}