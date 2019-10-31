package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.tjeneste.AbstractConfig;

@ConfigurationProperties("historikk")
@ConstructorBinding
public class HistorikkConfig extends AbstractConfig {
    private static final String DEFAULT_PING_PATH = "actuator/info";

    private final String key;

    public HistorikkConfig(URI uri, String key, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
        this.key = key;
    }

    public URI historikkURI() {
        return uri(getUri(), "historikk" + "/me/all");
    }

    public URI historikkPreprodURI(String fnr) {
        return uri(getUri(), "historikk" + "/dev/all", queryParams("fnr", fnr));
    }

    public URI pingURI() {
        return uri(getUri(), DEFAULT_PING_PATH);
    }

    public String getKey() {
        return key;
    }

    public URI minidialogURI() {
        return uri(getUri(), "/me/minidialoger/spm");

    }
}
