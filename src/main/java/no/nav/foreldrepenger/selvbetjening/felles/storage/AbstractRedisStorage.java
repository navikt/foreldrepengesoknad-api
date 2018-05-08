package no.nav.foreldrepenger.selvbetjening.felles.storage;

import redis.clients.jedis.Jedis;

public abstract class AbstractRedisStorage implements Storage {

    private static final int twoDays = 3600 * 24 * 2;

    protected abstract Jedis jedis();

    @Override
    public void put(String key, String value) {
        try (Jedis jedis = jedis()) {
            jedis.setex(key, twoDays, value);
        }
    }

    @Override
    public String get(String key) {
        try (Jedis jedis = jedis()) {
            return jedis.get(key);
        }
    }

    @Override
    public void delete(String key) {
        try (Jedis jedis = jedis()) {
            jedis.del(key);
        }
    }

}
