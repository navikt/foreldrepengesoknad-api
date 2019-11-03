package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Duration;

import com.google.api.gax.retrying.RetrySettings;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

public class GCPMellomlagring extends AbstractMellomlagringTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(GCPMellomlagring.class);

    private final Storage storage;

    public GCPMellomlagring(String søknadBøtte, String mellomlagringBøtte, boolean enabled) {
        super(søknadBøtte, mellomlagringBøtte, enabled);

        var retrySettings = RetrySettings.newBuilder()
                .setTotalTimeout(Duration.ofSeconds(5))
                .build();
        this.storage = StorageOptions
                .newBuilder()
                .setRetrySettings(retrySettings)
                .build()
                .getService();
    }

    @Override
    public URI pingURI() {
        return URI.create("https://storage.googleapis.com");
    }

    @Override
    protected void lagre(String bøtte, String katalog, String key, String value) {
        try {
            storage.create(BlobInfo.newBuilder(BlobId.of(bøtte, key(katalog, key)))
                    .setContentType(APPLICATION_JSON_VALUE).build(), value.getBytes(UTF_8));
        } catch (StorageException e) {
            throw new MellomlagringException(e);
        }
    }

    @Override
    protected String les(String bøtte, String katalog, String key) {
        try {
            return Optional.ofNullable(storage.get(bøtte, key(katalog, key)))
                    .map(Blob::getContent)
                    .filter(Objects::nonNull)
                    .map(b -> new String(b, UTF_8))
                    .orElse(null);
        } catch (StorageException e) {
            return null;
        }
    }

    @Override
    protected void slett(String bøtte, String katalog, String key) {
        try {
            storage.delete(BlobId.of(bøtte, key(katalog, key)));
        } catch (StorageException e) {
            throw new MellomlagringException(e);
        }
    }

    @Override
    public String name() {
        return pingURI().getHost();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[storage=" + storage + ", søknadBøtte=" + getSøknadBøtte()
                + ", mellomlagringBøtte=" + getMellomlagringBøtte() + "]";
    }

}
