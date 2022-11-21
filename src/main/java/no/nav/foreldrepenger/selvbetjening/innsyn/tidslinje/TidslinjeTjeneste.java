package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;
import java.util.Map;

@Service
@ConditionalOnNotProd
public class TidslinjeTjeneste extends AbstractRestConnection {

    private final static URI BASE_URI = URI.create("https://fpinfo-historikk.dev-fss-pub.nais.io/api/");
    private final static String TIDSLINJE_URI_TMPL = BASE_URI + "tidslinje/{saksnummer}";


    @Inject
    public TidslinjeTjeneste(RestOperations operations) {
        super(operations);
    }
    public String hentTidslinje(String saksnummer) {
        var uri = UriComponentsBuilder.fromHttpUrl(TIDSLINJE_URI_TMPL)
            .buildAndExpand(Map.of("saksnummer", saksnummer))
            .toUri();
        return getForObject(uri, String.class);
    }

    @Override
    public URI pingURI() {
        return UriComponentsBuilder.fromUri(BASE_URI)
            .pathSegment("actuator/health/liveness")
            .build().toUri();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
