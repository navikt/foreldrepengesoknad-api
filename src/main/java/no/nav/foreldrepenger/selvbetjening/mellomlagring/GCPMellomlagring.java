package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.gax.retrying.RetrySettings;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

public class GCPMellomlagring extends AbstractMellomlagringTjeneste {

    private static final URI STORAGE = URI.create("https://storage.googleapis.com");

    private static final Logger LOG = LoggerFactory.getLogger(GCPMellomlagring.class);

    private final Storage storage;

    public GCPMellomlagring(Bøtte søknadBøtte, Bøtte mellomlagringBøtte, RetrySettings retrySettings) {
        super(søknadBøtte, mellomlagringBøtte);
        this.storage = StorageOptions
                .newBuilder()
                .setRetrySettings(retrySettings)
                .build()
                .getService();
    }

    @Override
    protected void doLagre(String bøttenavn, String katalog, String key, String value) {
        storage.create(BlobInfo.newBuilder(blobFra(bøttenavn, katalog, key))
                .setContentType(APPLICATION_JSON_VALUE).build(), value.getBytes(UTF_8));
    }

    @Override
    protected Optional<String> doLes(String bøtte, String katalog, String key) {
        try {
            return Optional.ofNullable(storage.get(bøtte, key(katalog, key)))
                    .map(Blob::getContent)
                    .filter(Objects::nonNull)
                    .map(b -> new String(b, UTF_8));
        } catch (StorageException e) {
            if (SC_NOT_FOUND == e.getCode()) {
                return Optional.empty();
            }
            LOG.warn("Katalog {} ikke funnet, ({})", katalog, e.getCode());
            throw e;
        }
    }

    @Override
    protected void doSlett(String bøtte, String katalog, String key) {
        storage.delete(blobFra(bøtte, katalog, key));
    }

    @Override
    protected void validerBøtte(Bøtte bøtte) {
        storage.get(bøtte.getNavn());
    }

    private static BlobId blobFra(String bøttenavn, String katalog, String key) {
        return BlobId.of(bøttenavn, key(katalog, key));
    }

    @Override
    public URI pingURI() {
        return STORAGE;
    }

    @Override
    public String name() {
        return ("GCPMellomlagring");
    }
}
