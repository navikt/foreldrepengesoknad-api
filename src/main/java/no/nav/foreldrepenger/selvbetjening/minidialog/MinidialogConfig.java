package no.nav.foreldrepenger.selvbetjening.minidialog;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties(MinidialogConfig.MINIDIALOG)
public class MinidialogConfig extends AbstractConfig {

    static final String MINIDIALOG = "minidialog";

    private static final String DEFAULT_PING_PATH = "actuator/info";

    public MinidialogConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    public URI aktiveSpmURI() {
        return uri(getBaseUri(), MINIDIALOG + "/me");
    }

    @Override
    public URI pingURI() {
        return uri(getBaseUri(), DEFAULT_PING_PATH);
    }

}
