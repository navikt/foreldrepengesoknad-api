package no.nav.foreldrepenger.selvbetjening.minidialog;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(MinidialogConfig.MINIDIALOG)
public class MinidialogConfig {

    static final String MINIDIALOG = "minidialog";
    private final URI baseUri;

    public MinidialogConfig(URI uri) {
        this.baseUri = uri;
    }

    public URI aktiveSpmURI() {
        return uri(getBaseUri(), MINIDIALOG + "/me");
    }

    private URI getBaseUri() {
        return baseUri;
    }
}
