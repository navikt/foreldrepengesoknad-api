package no.nav.foreldrepenger.selvbetjening.stub;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.AbstractMellomlagringTjeneste;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.Bøtte;
import no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring.MellomlagringType;

public class InMemoryMellomlagring extends AbstractMellomlagringTjeneste {

    private final Map<String, String> store;

    public InMemoryMellomlagring(Bøtte b1, Bøtte b2) {
        super(b1, b2);
        store = new HashMap<String, String>();
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
    protected String doLes(String bøtte, String katalog, String key) {
        return store.get(key(katalog, key));
    }

    @Override
    protected void doSlett(String bøtte, String katalog, String key) {
        store.remove(key(katalog, key));
    }

}
