package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import no.nav.boot.conditionals.ConditionalOnLocalOrTest;

@Service
@ConditionalOnLocalOrTest
public class InMemoryMellomlagring implements Mellomlagring {

    private final Map<String, String> store;

    public InMemoryMellomlagring() {
        store = new HashMap<>();
    }

    @Override
    public void lagre(String katalog, String key, String value) {
        store.put(key(katalog, key), value);

    }

    @Override
    public Optional<String> les(String katalog, String key) {
        return Optional.ofNullable(store.get(key(katalog, key)));
    }

    @Override
    public void slett(String katalog, String key) {
        store.remove(key(katalog, key));
    }

    private static String key(String directory, String key) {
        return directory + "_" + key;
    }
}
