package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import no.nav.boot.conditionals.ConditionalOnGCP;

@Component
@ConditionalOnGCP
public class GCPMellomlagring implements Mellomlagring {

    private static final Logger LOG = LoggerFactory.getLogger(GCPMellomlagring.class);

    private final Bøtte mellomlagringBøtte;
    private final Storage storage;

    public GCPMellomlagring(Bøtte mellomlagringBøtte, Storage storage) {
        this.mellomlagringBøtte = mellomlagringBøtte;
        this.storage = storage;
    }

    @Override
    public void lagre(String katalog, String key, String value) {
        storage.create(BlobInfo.newBuilder(blobFra(mellomlagringBøtte, katalog, key))
                .setContentType(APPLICATION_JSON_VALUE).build(), value.getBytes(UTF_8));
    }

    @Override
    public void lagreVedlegg(String katalog, String key, byte[] value) {
        storage.create(BlobInfo.newBuilder(blobFra(mellomlagringBøtte, katalog, key))
            .setContentType(APPLICATION_OCTET_STREAM_VALUE).build(), value);
    }

    @Override
    public boolean eksisterer(String katalog, String key) {
        return Optional.ofNullable(storage.get(mellomlagringBøtte.navn(), key(katalog, key))).isPresent();
    }

    @Override
    public Optional<String> les(String katalog, String key) {
        return Optional.ofNullable(storage.get(mellomlagringBøtte.navn(), key(katalog, key)))
            .map(Blob::getContent)
            .map(b -> new String(b, UTF_8));
    }

    @Override
    public Optional<byte[]> lesVedlegg(String katalog, String key) {
        return Optional.ofNullable(storage.get(mellomlagringBøtte.navn(), key(katalog, key))).map(Blob::getContent);
    }

    @Override
    public void slett(String katalog, String key) {
        var objektName = key(katalog, key);
        var blob = storage.get(mellomlagringBøtte.navn(), objektName);
        if (blob != null) {
            LOG.info("Sletter mellomlagring med id {}", blob.getBlobId());
            storage.delete(blob.getBlobId());
        } else {
            LOG.info("Kunne ikke finne mellomlagring som skulle slettes med id {}", objektName);
        }
    }

    @Override
    public void slettAll(String katalog) {
        var blobs = storage.list(
            mellomlagringBøtte.navn(),
            Storage.BlobListOption.prefix(katalog),
            Storage.BlobListOption.currentDirectory()
        );

        if (blobs.streamAll().findAny().isPresent()) {
            var batch = storage.batch();
            for (var blob : blobs.iterateAll()) {
                LOG.trace("Legger til {} for sletting i batch", blob.getName());
                batch.delete(blob.getBlobId());
            }
            batch.submit();
            LOG.info("Alle blobs i bøtte {} med prefiks {} er blitt slettet", mellomlagringBøtte.navn(), katalog);
        }
    }

    private static BlobId blobFra(Bøtte bøtte, String katalog, String key) {
        return BlobId.of(bøtte.navn(), key(katalog, key));
    }

    private static String key(String directory, String key) {
        return directory + key;
    }
}
