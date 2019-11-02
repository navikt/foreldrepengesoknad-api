package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.util.Optional;

import no.nav.foreldrepenger.selvbetjening.tjeneste.PingEndpointAware;

public interface MellomlagringTjeneste extends PingEndpointAware {

    void lagre(String directory, String key, String value);

    void lagreTmp(String directory, String key, String value);

    Optional<String> les(String directory, String key);

    Optional<String> lesTmp(String directory, String key);

    void slett(String directory, String key);

    void slettTmp(String directory, String key);
}
