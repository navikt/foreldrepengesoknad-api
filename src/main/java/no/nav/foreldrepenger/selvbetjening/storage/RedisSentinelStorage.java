package no.nav.foreldrepenger.selvbetjening.storage;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class RedisSentinelStorage extends AbstractStorage {

    private JedisSentinelPool pool;

    private static final Set<String> sentinels = new HashSet<>();
    private static final int twoDays = 3600 * 24 * 2;

    public RedisSentinelStorage() {
        sentinels.add("rfs-foreldrepengesoknad-api:26379");
        pool = new JedisSentinelPool("mymaster", sentinels);
    }

    @Override
    protected Jedis jedis() {
        return pool.getResource();
    }

}
