package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;

@Service
@ConditionalOnNotProd
public class DokumentArkivTjeneste extends AbstractRestConnection {

    private URI baseUri;


    @Inject
    public DokumentArkivTjeneste(@Value("${historikk.uri}") URI baseUri,
                                 RestOperations operations) {
        super(operations);
        this.baseUri = baseUri;
    }

    public byte[] hentDokument(String journalpostId, String dokumentId) {
        return getForObject(dokUri(journalpostId, dokumentId), byte[].class);
    }

    public String hentDokumentoversikt() {
        return getForObject(dokumenterUri(), String.class);
    }

    private URI dokUri(String journalpostId, String dokumentId) {
        return UriComponentsBuilder.fromUri(baseUri)
            .pathSegment("arkiv", "hent-dokument", journalpostId, dokumentId)
            .build().toUri();
    }

    @Override
    public URI pingURI() {
        return UriComponentsBuilder.fromUri(baseUri)
            .pathSegment("actuator/health/liveness")
            .build().toUri();
    }

    private URI dokumenterUri() {
        return UriComponentsBuilder.fromUri(baseUri)
            .pathSegment("arkiv", "alle")
            .build().toUri();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
