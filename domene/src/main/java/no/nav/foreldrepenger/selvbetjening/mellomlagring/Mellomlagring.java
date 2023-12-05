package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import java.util.Optional;

import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

public interface Mellomlagring extends RetryAware {

    void lagre(String katalog, String key, String value, boolean mappestruktur);

    Optional<String> les(String directory, String key, boolean mappestruktur);

    void slett(String directory, String key, boolean mappestruktur);

    void slettAll(String katalog);
}
