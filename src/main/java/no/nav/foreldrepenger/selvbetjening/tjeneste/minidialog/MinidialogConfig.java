package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.util.Pair;

@ConfigurationProperties(MinidialogConfig.MINIDIALOG)
@ConstructorBinding
public class MinidialogConfig extends AbstractConfig {

    static final String MINIDIALOG = "minidialog";
    private static final String MINIDIALOG_DEV = MINIDIALOG + "/dev";
    private static final String MINIDIALOGER = MINIDIALOG_DEV + "/minidialoger";
    private static final String AKTIVE = MINIDIALOG_DEV + "/spm";

    private static final String DEFAULT_PING_PATH = "actuator/info";
    private final boolean enabled;
    private final URI uri;

    public MinidialogConfig(URI uri, @DefaultValue("true") boolean enabled) {
        this.uri = uri;
        this.enabled = enabled;
    }

    public URI getURI() {
        return uri;
    }

    public URI minidialogPreprodURI(String fnr, boolean activeOnly) {
        return uri(getURI(), MINIDIALOGER, queryParams(Pair.of("fnr", fnr), Pair.of("activeOnly", activeOnly)));
    }

    public URI aktiveSpmURI() {
        return uri(getURI(), MINIDIALOG + "/me");
    }

    public URI aktiveSpmURI(String fnr) {
        return uri(getURI(), AKTIVE, queryParams(Pair.of("fnr", fnr)));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public URI pingURI() {
        return uri(getURI(), DEFAULT_PING_PATH);
    }

}
