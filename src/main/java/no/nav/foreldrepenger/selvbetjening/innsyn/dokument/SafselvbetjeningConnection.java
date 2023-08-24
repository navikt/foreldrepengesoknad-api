package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.http.RetryAware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class SafselvbetjeningConnection extends AbstractRestConnection implements RetryAware {

    private static final String HENT_DOKUMENT_PATH_TMPL = "/rest/hentdokument/{journalpostId}/{dokumentInfoId}/ARKIV";
    private final URI baseUri;

    @Autowired
    protected SafselvbetjeningConnection(@Value("${safselvbetjening.baseUri}") URI baseUri,
                                         RestOperations operations) {
        super(operations);
        this.baseUri = baseUri;
    }

    public ResponseEntity<byte[]> hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        var uriTemplate = UriComponentsBuilder.fromUri(baseUri).path(HENT_DOKUMENT_PATH_TMPL).build().toUriString();
        return getForEntity(uriTemplate, byte[].class, journalpostId, dokumentId);
    }

}
