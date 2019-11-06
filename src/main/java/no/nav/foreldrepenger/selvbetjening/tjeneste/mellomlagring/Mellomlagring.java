package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.util.Optional;

import no.nav.foreldrepenger.selvbetjening.tjeneste.PingEndpointAware;

public interface Mellomlagring extends PingEndpointAware {

    void lagre(MellomlagringType type, String katalog, String key, String value);

    Optional<String> les(MellomlagringType type, String directory, String key);

    void slett(MellomlagringType type, String directory, String key);

}
