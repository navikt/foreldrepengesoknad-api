package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import no.nav.boot.conditionals.ConditionalOnLocalOrTest;

@Service
@ConditionalOnLocalOrTest
public class InMemoryMellomlagring extends AbstractMellomlagringTjeneste {

    private final Map<String, String> store;

    public InMemoryMellomlagring(Bøtte bøtte) {
        super(bøtte);
        store = new HashMap<>();
    }

    @Override
    public void slett(MellomlagringType type, String katalog, String key) {
        store.remove(key(katalog, key));
    }

    @Override
    protected void validerBøtte(Bøtte bøtte) {

    }

    @Override
    protected void doLagre(String bøtte, String katalog, String key, String value) {
        store.put(key(katalog, key), value);

    }

    @Override
    protected Optional<String> doLes(String bøtte, String katalog, String key) {
        return Optional.ofNullable(store.get(key(katalog, key)));
    }

    @Override
    protected void doSlett(String bøtte, String katalog, String key) {
        store.remove(key(katalog, key));
    }

}
