package no.nav.foreldrepenger.selvbetjening.storage;

import redis.clients.jedis.Jedis;

public class RedisStandalonelStorage extends AbstractRedisStorage {

    private String host;
    private int port;

    public RedisStandalonelStorage(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected Jedis jedis() {
        return new Jedis(host, port);
    }

}
