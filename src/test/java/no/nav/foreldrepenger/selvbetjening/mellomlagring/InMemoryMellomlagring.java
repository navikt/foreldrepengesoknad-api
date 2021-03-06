package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.mellomlagring.Bøtte.SØKNAD;
import static no.nav.foreldrepenger.selvbetjening.mellomlagring.Bøtte.TMP;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.boot.conditionals.ConditionalOnLocal;

@Service
@ConditionalOnLocal
public class InMemoryMellomlagring extends AbstractMellomlagringTjeneste {

    private final Map<String, String> store;

    public InMemoryMellomlagring(@Qualifier(SØKNAD) Bøtte b1, @Qualifier(TMP) Bøtte b2) {
        super(b1, b2);
        store = new HashMap<>();
    }

    @Override
    public void slett(MellomlagringType type, String katalog, String key) {
        store.remove(key(katalog, key));
    }

    @Override
    public String ping() {
        return "OK";
    }

    @Override
    public URI pingURI() {
        return URI.create("http://localhost/stub");
    }

    @Override
    public String name() {
        return pingURI().getHost();
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

    @Override
    public boolean isEnabled() {
        return true;
    }

}
