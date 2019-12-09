package no.nav.foreldrepenger.selvbetjening.tjeneste.mellomlagring;

import java.util.Optional;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.amazonaws.SdkClientException;
import com.google.cloud.storage.StorageException;

import no.nav.foreldrepenger.selvbetjening.tjeneste.PingEndpointAware;

@Retryable(include = {
        SdkClientException.class,
        StorageException.class }, maxAttemptsExpression = "#{${rest.retry.attempts:3}}", backoff = @Backoff(delayExpression = "#{${rest.retry.delay:1000}}"))

public interface Mellomlagring extends PingEndpointAware {

    void lagre(MellomlagringType type, String katalog, String key, String value);

    Optional<String> les(MellomlagringType type, String directory, String key);

    void slett(MellomlagringType type, String directory, String key);

}
