package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import java.util.Optional;

import no.nav.foreldrepenger.selvbetjening.http.PingEndpointAware;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

public interface Mellomlagring extends PingEndpointAware, RetryAware {

    void lagre(MellomlagringType type, String katalog, String key, String value);

    Optional<String> les(MellomlagringType type, String directory, String key);

    void slett(MellomlagringType type, String directory, String key);

}
