package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

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

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

@Service
@Deprecated
public class DokumentArkivTjeneste extends AbstractRestConnection implements RetryAware {

    private final URI baseUri;


    @Autowired
    public DokumentArkivTjeneste(@Value("${historikk.uri}") URI baseUri,
                                 RestOperations operations) {
        super(operations);
        this.baseUri = baseUri;
    }

    public byte[] hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return getForObject(dokUri(journalpostId, dokumentId), byte[].class);
    }

    public List<ArkivDokument> hentDokumentoversikt() {
        return Optional.ofNullable(getForObject(dokumenterUri(), ArkivDokument[].class))
            .map(Arrays::asList)
            .orElse(emptyList());
    }

    private URI dokUri(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return UriComponentsBuilder.fromUri(baseUri)
            .pathSegment("arkiv", "hent-dokument", journalpostId.value(), dokumentId.value())
            .build().toUri();
    }

    private URI dokumenterUri() {
        return UriComponentsBuilder.fromUri(baseUri)
            .pathSegment("arkiv", "alle")
            .build().toUri();
    }
}
