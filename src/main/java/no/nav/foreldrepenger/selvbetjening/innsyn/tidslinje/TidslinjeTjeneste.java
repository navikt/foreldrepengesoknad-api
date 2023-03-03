package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;

@Service
public class TidslinjeTjeneste extends AbstractRestConnection implements RetryAware {
    private final URI baseUri;

    @Inject
    public TidslinjeTjeneste(RestOperations operations,
                             @Value("${historikk.uri}") URI uri) {
        super(operations);
        this.baseUri = uri;
    }

    public String hentTidslinje(Saksnummer saksnummer) {
        var uri = UriComponentsBuilder.fromUri(baseUri)
            .pathSegment("historikk", "tidslinje")
            .queryParam("saksnummer", saksnummer)
            .build()
            .toUri();
        return getForObject(uri, String.class);
    }

    @Override
    public URI pingURI() {
        return UriComponentsBuilder.fromUri(baseUri)
            .pathSegment("actuator/health/liveness")
            .build().toUri();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
