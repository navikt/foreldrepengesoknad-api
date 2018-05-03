package no.nav.foreldrepenger.selvbetjening.storage;

import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StorageTest {

    @Rule
    public GenericContainer redis = new GenericContainer("redis:4.0.9")
            .withExposedPorts(6379);

    @Test
    public void storeAndReadBack() {
        Storage storage = new RedisStandalonelStorage(redis.getContainerIpAddress(),
                redis.getMappedPort(6379));
        storage.put("mykey", "myvalue");
        assertEquals("myvalue", storage.get("mykey"));
    }

    @Test
    public void deleteAndReadBack() {
        Storage storage = new RedisStandalonelStorage(redis.getContainerIpAddress(),
                redis.getMappedPort(6379));
        storage.put("mykey", "myvalue");
        storage.delete("mykey");
        assertNull(storage.get("mykey"));
    }

}
