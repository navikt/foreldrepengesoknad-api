package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;

@Service
public class DokumentArkivTjeneste extends AbstractRestConnection implements RetryAware {

    private final URI baseUri;


    @Inject
    public DokumentArkivTjeneste(@Value("${historikk.uri}") URI baseUri,
                                 RestOperations operations) {
        super(operations);
        this.baseUri = baseUri;
    }

    public byte[] hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return getForObject(dokUri(journalpostId, dokumentId), byte[].class);
    }

    public String hentDokumentoversikt() {
        return getForObject(dokumenterUri(), String.class);
    }

    private URI dokUri(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return UriComponentsBuilder.fromUri(baseUri)
            .pathSegment("arkiv", "hent-dokument", journalpostId.value(), dokumentId.value())
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
