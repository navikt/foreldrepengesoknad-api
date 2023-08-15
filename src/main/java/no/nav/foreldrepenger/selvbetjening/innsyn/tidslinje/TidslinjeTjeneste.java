package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

@Service
public class TidslinjeTjeneste extends AbstractRestConnection implements RetryAware {
    private final URI baseUri;

    @Autowired
    public TidslinjeTjeneste(RestOperations operations,
                             @Value("${historikk.uri}") URI uri) {
        super(operations);
        this.baseUri = uri;
    }

    public String hentTidslinje(Saksnummer saksnummer) {
        var uri = UriComponentsBuilder.fromUri(baseUri)
            .pathSegment("historikk", "tidslinje")
            .queryParam("saksnummer", saksnummer.value())
            .build()
            .toUri();
        return getForObject(uri, String.class);
    }
}
