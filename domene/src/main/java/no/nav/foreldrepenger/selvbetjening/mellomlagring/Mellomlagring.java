package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import java.util.Optional;

import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

public interface Mellomlagring extends RetryAware {

    void lagre(String katalog, String key, String value);
    void lagreVedlegg(String katalog, String key, byte[] value);

    boolean eksisterer(String katalog, String key);

    Optional<String> les(String directory, String key);
    Optional<byte[]> lesVedlegg(String directory, String key);

    void slett(String directory, String key);

    void slettAll(String katalog);

    void oppdaterMellomlagredeVedleggOgSøknad(String katalog);
}
