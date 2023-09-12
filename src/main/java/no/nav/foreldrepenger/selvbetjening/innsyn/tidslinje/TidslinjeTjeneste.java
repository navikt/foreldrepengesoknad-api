package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import static java.util.Collections.emptyList;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public List<TidslinjeHendelseDto> hentTidslinje(Saksnummer saksnummer) {
        var uri = UriComponentsBuilder.fromUri(baseUri)
            .pathSegment("historikk", "tidslinje")
            .queryParam("saksnummer", saksnummer.value())
            .build()
            .toUri();
        return Optional.ofNullable(getForObject(uri, TidslinjeHendelseDto[].class))
            .map(Arrays::asList)
            .orElse(emptyList());
    }
}
