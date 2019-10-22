package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;

public class GCPCloudStorage implements Storage {

    private static final Logger LOG = LoggerFactory.getLogger(GCPCloudStorage.class);

    private final com.google.cloud.storage.Storage storage;
    private final String søknadBucket;
    private final String mellomlagringBucket;

    public GCPCloudStorage(@Value("${bucket.søknad:foreldrepengesoknad}") String søknadBucket,
            @Value("${bucket.mellomlagring:mellomlagring}") String mellomlagringBucket) {
        storage = StorageOptions.getDefaultInstance().getService();
        this.søknadBucket = søknadBucket;
        this.mellomlagringBucket = mellomlagringBucket;
    }

    @Override
    public String ping() {
        put("ping", "pingKey", "42");
        delete("ping", "pingKey");
        return "OK";
    }

    @Override
    public URI pingURI() {
        return URI.create("https://storage.googleapis.com");
    }

    @Override
    public void put(String directory, String key, String value) {
        writeString(søknadBucket, directory, key, value);
    }

    @Override
    public void putTmp(String directory, String key, String value) {
        writeString(mellomlagringBucket, directory, key, value);
    }

    @Override
    public Optional<String> get(String directory, String key) {
        return Optional.ofNullable(readString(søknadBucket, directory, key));
    }

    @Override
    public Optional<String> getTmp(String directory, String key) {
        return Optional.ofNullable(readString(mellomlagringBucket, directory, key));
    }

    @Override
    public void delete(String directory, String key) {
        deleteString(søknadBucket, directory, key);
    }

    private void deleteString(String bucketName, String directory, String key) {
        LOG.info("Fjerner objekt fra bøtte {}, katalog {}", bucketName, directory);
        storage.delete(BlobId.of(bucketName, fileName(directory, key)));
        LOG.info("Fjernet objekt {} fra bøtte {}", directory, bucketName);
    }

    @Override
    public void deleteTmp(String directory, String key) {
        deleteString(mellomlagringBucket, directory, key);
    }

    private void writeString(String bucketName, String directory, String key, String value) {
        LOG.info("Lagrer objekt i bøtte {}, katalog {}", bucketName, directory);
        Blob blob = storage.create(
                BlobInfo.newBuilder(BlobId.of(bucketName, fileName(directory, key)))
                        .setContentType(APPLICATION_JSON_UTF8_VALUE).build(),
                value.getBytes(UTF_8));
        LOG.info("Lagret objekt {} i bøtte {}", blob, bucketName);
    }

    private String readString(String bucketName, String directory, String key) {
        String path = fileName(directory, key);
        try {
            LOG.info("Henter objekt fra bøtte {}, katalog {}", bucketName, directory);
            String value = new String(storage.get(bucketName, path).getContent(), StandardCharsets.UTF_8);
            LOG.info("Hentet objekt {} fra bøtte {}", value, bucketName);
            return value;
        } catch (Exception e) {
            LOG.trace("Kunne ikke hente {}, finnes sannsynligvis ikke", path, e);
            return null;
        }
    }

    private static String fileName(String directory, String key) {
        return directory + "_" + key;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[storage=" + storage + ", søknadBucket=" + søknadBucket
                + ", mellomlagringBucket=" + mellomlagringBucket + "]";
    }
}
