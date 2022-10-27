package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;

@Service
@ConditionalOnNotProd
public class DokumentArkivTjeneste extends AbstractRestConnection {

    private final static URI BASE_URI = URI.create("https://fpinfo-historikk.dev-fss-pub.nais.io/api/");
    private final static URI DOKUMENTER_URI = UriComponentsBuilder.fromUri(BASE_URI)
        .pathSegment("arkiv", "alle")
        .build().toUri();


    @Inject
    public DokumentArkivTjeneste(RestOperations operations) {
        super(operations);
    }

    public byte[] hentDokument(String dokumentId) {
        return getForObject(dokUri(dokumentId), byte[].class);
    }

    public String hentDokumentoversikt() {
        return getForObject(DOKUMENTER_URI, String.class);
    }

    private URI dokUri(String dokumentId) {
        return UriComponentsBuilder.fromUri(BASE_URI)
            .pathSegment("arkiv", "hent-dokument")
            .queryParam("dokumentId", dokumentId)
            .build().toUri();
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
