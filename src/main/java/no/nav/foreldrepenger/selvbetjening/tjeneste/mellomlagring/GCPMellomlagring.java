package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

public class GCPMellomlagring extends AbstractMellomlagringTjeneste implements EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(GCPMellomlagring.class);

    private final Storage storage;

    private Environment env;

    public GCPMellomlagring(String søknadBøtte, String mellomlagringBøtte) {
        super(søknadBøtte, mellomlagringBøtte);
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    @Override
    public URI pingURI() {
        return URI.create("https://storage.googleapis.com");
    }

    @Override
    protected boolean lagre(String bøtte, String katalog, String key, String value) {
        try {
            storage.create(BlobInfo.newBuilder(BlobId.of(bøtte, fileName(katalog, key)))
                    .setContentType(APPLICATION_JSON_VALUE).build(), value.getBytes(UTF_8));
            return true;
        } catch (StorageException e) {
            LOG.warn("Feil ved lagring", e);
            return false;
        }
    }

    @Override
    protected String les(String bøtte, String katalog, String key) {
        try {
            return new String(storage.get(bøtte, fileName(katalog, key)).getContent(), UTF_8);
        } catch (StorageException e) {
            LOG.warn("Feil ved henting", e);
            return null;
        }
    }

    @Override
    protected boolean slett(String bøtte, String katalog, String key) {
        try {
            storage.delete(BlobId.of(bøtte, fileName(katalog, key)));
            return true;
        } catch (StorageException e) {
            LOG.warn("Feil ved fjerning", e);
            return false;
        }
    }

    @Override
    public boolean isEnabled() {
        return env.getProperty("mellomlagring.gcp.enabled", boolean.class, true);
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
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
