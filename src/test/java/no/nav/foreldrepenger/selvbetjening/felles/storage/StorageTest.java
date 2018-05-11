package no.nav.foreldrepenger.selvbetjening.felles.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import no.nav.foreldrepenger.selvbetjening.SlowTests;
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
    public void storeAndReadBack() {
        Storage storage = new S3Storage(s3());
        storage.put("mydir", "mykey", "myvalue");
        assertEquals(Optional.of("myvalue"), storage.get("mydir", "mykey"));
    }

    @Test
    public void deleteAndReadBack() {
        Storage storage = new S3Storage(s3());
        storage.put("mydir", "mykey", "myvalue");
        storage.delete("mydir", "mykey");
        assertEquals(Optional.empty(), storage.get("mydir", "mykey"));
    }

    private AmazonS3 s3() {
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(localstack.getEndpointConfiguration(S3))
                .withCredentials(localstack.getDefaultCredentialsProvider())
                .build();
    }

}
