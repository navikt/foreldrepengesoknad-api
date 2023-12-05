package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import no.nav.boot.conditionals.ConditionalOnLocalOrTest;

@Service
@ConditionalOnLocalOrTest
@ConditionalOnMissingBean(GCPMellomlagring.class)
public class InMemoryMellomlagring implements Mellomlagring {

    private final Map<String, String> store;

    public InMemoryMellomlagring() {
        store = new HashMap<>();
    }

    @Override
    public void lagre(String katalog, String key, String value, boolean mappestruktur) {
        store.put(key(katalog, key), value);
    }

    @Override
    public Optional<String> les(String katalog, String key, boolean mappestruktur) {
        return Optional.ofNullable(store.get(key(katalog, key)));
    }

    @Override
    public void slett(String katalog, String key, boolean mappestruktur) {
        store.remove(key(katalog, key));
    }

    @Override
    public void slettAll(String katalog) {
        var alleNøkkler = store.keySet().stream()
            .filter(k -> k.startsWith(katalog))
            .collect(Collectors.toSet());
        for (var nøkkel : alleNøkkler) {
            store.remove(nøkkel);
        }
    }

    private static String key(String directory, String key) {
        return directory + key;
    }
}
