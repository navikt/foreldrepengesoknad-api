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

    private final Map<String, Object> store;


    public InMemoryMellomlagring() {
        store = new HashMap<>();
    }

    @Override
    public void lagre(String katalog, String key, String value) {
        store.put(key(katalog, key), value);
    }

    @Override
    public void lagreVedlegg(String katalog, String key, byte[] value) {
        store.put(key(katalog, key), value);
    }

    @Override
    public boolean eksisterer(String katalog, String key) {
        return store.containsKey(key(katalog, key));
    }

    @Override
    public Optional<String> les(String katalog, String key) {
        return Optional.ofNullable(((String) store.get(key(katalog, key))));
    }

    @Override
    public Optional<byte[]> lesVedlegg(String katalog, String key) {
        return Optional.ofNullable(((byte[]) store.get(key(katalog, key))));
    }

    @Override
    public void slett(String katalog, String key) {
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

    @Override
    public void oppdaterMellomlagredeVedleggOgSøknad(String katalog) {
        // do nothign
    }

    private static String key(String directory, String key) {
        return directory + key;
    }
}
