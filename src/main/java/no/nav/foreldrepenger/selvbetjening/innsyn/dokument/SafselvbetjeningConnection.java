package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class SafselvbetjeningConnection extends AbstractRestConnection implements RetryAware {

    private static final String HENT_DOKUMENT_PATH_TMPL = "/rest/hentdokument/{journalpostId}/{dokumentInfoId}/{variantFormat}";
    private static final String DEFAULT_VARIANTFORMAT = "ARKIV";
    private final URI baseUri;

    @Autowired
    protected SafselvbetjeningConnection(@Value("${safselvbetjening.baseUri}") URI baseUri,
                                         RestOperations operations) {
        super(operations);
        this.baseUri = baseUri;
    }

    public ResponseEntity<byte[]> hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return getForEntity(uri(journalpostId, dokumentId), byte[].class);
    }

    private URI uri(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return UriComponentsBuilder.fromUri(baseUri)
            .path(HENT_DOKUMENT_PATH_TMPL)
            .queryParam("journalpostId", journalpostId.value())
            .queryParam("dokumentInfoId", dokumentId.value())
            .queryParam("variantFormat", DEFAULT_VARIANTFORMAT)
            .build().toUri();
    }

}
