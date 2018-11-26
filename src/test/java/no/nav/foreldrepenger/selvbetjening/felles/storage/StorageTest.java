package no.nav.foreldrepenger.selvbetjening.felles.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import no.nav.foreldrepenger.selvbetjening.SlowTests;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.S3Storage;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Storage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;


@Category(SlowTests.class)
public class StorageTest {

    @Rule
    public LocalStackContainer localstack = new LocalStackContainer()
            .withServices(S3);

    @Test
    public void storeAndRead() {
        Storage storage = new S3Storage(s3());
        storage.put("mydir", "mykey", "myvalue");
        assertEquals(Optional.of("myvalue"), storage.get("mydir", "mykey"));
    }

    @Test
    public void deleteAndRead() {
        Storage storage = new S3Storage(s3());
        storage.put("mydir", "mykey", "myvalue");
        storage.delete("mydir", "mykey");
        assertEquals(Optional.empty(), storage.get("mydir", "mykey"));
    }

    @Test
    public void createBucketIsIdempotent() {
        new S3Storage(s3());
        new S3Storage(s3());
    }

    @Test
    public void deleteIsIdempotent() {
        Storage storage = new S3Storage(s3());
        storage.put("mydir", "mykey", "myvalue");
        storage.delete("mydir", "mykey");
        storage.delete("mydir", "mykey");
    }


    @Test
    public void tmpStorageIsInDifferentBucketThanRegularStore() {
        Storage storage = new S3Storage(s3());
        storage.putTmp("tmpdir", "tmpkey", "tmpvalue");
        assertEquals(Optional.empty(), storage.get("tmpdir", "tmpkey"));
        storage.put("mydir", "mykey", "myvalue");
        assertEquals(Optional.empty(), storage.getTmp("mydir", "mykey"));

    }


    @Test
    public void tmpStoreAndReadAndDelete() {
        Storage storage = new S3Storage(s3());
        storage.putTmp("mydir", "mykey", "myvalue");
        assertEquals(Optional.of("myvalue"), storage.getTmp("mydir", "mykey"));
        storage.deleteTmp("mydir", "mykey");
        assertEquals(Optional.empty(), storage.getTmp("mydir", "mykey"));

    }

    private AmazonS3 s3() {
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(localstack.getEndpointConfiguration(S3))
                .withCredentials(localstack.getDefaultCredentialsProvider())
                .enablePathStyleAccess()
                .build();
    }
}
