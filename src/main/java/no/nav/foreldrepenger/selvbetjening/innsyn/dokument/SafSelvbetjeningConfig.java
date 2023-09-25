package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@ConfigurationProperties(prefix = "safselvbetjening")
public class SafSelvbetjeningConfig {

    private static final String HENT_DOKUMENT_PATH = "/rest/hentdokument/{journalpostId}/{dokumentInfoId}/ARKIV";
    private static final String GRAPHQL_PATH = "/graphql";

    private final URI baseUri;

    protected SafSelvbetjeningConfig(URI uri) {
        this.baseUri = uri;
    }


    URI graphqlPath() {
        return uri(baseUri, GRAPHQL_PATH);
    }

    UriComponents hentDokument() {
        return UriComponentsBuilder.fromUri(baseUri).path(HENT_DOKUMENT_PATH).build();
    }
}
