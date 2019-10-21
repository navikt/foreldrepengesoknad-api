package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.drew.lang.Charsets;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;

public class GCPCloudStorage implements Storage {

    private static final String BUCKET_FORELDREPENGER = "foreldrepengesoknad";
    private static final String BUCKET_FORELDREPENGER_MELLOMLAGRING = "mellomlagring";

    private static final Logger LOG = LoggerFactory.getLogger(GCPCloudStorage.class);

    private final com.google.cloud.storage.Storage storage;

    public GCPCloudStorage() {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    @Override
    public String ping() {
        return "OK";
    }

    @Override
    public URI pingURI() {
        return URI.create("http://localhost");
    }

    @Override
    public void put(String directory, String key, String value) {
        writeString(BUCKET_FORELDREPENGER, directory, key, value);
    }

    @Override
    public void putTmp(String directory, String key, String value) {

    }

    @Override
    public Optional<String> get(String directory, String key) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getTmp(String directory, String key) {
        return Optional.empty();
    }

    @Override
    public void delete(String directory, String key) {

    }

    @Override
    public void deleteTmp(String directory, String key) {

    }

    private void writeString(String bucketName, String directory, String key, String value) {
        LOG.info("Lagrer object i bøtte {}, katalog {}", bucketName, directory);
        BlobId blobId = BlobId.of(bucketName, fileName(directory, key));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE).build();
        Blob blob = storage.create(blobInfo, value.getBytes(Charsets.UTF_8));
    }

    private static String fileName(String directory, String key) {
        return directory + "_" + key;
    }

}
