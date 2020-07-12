package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.util.Pair;

@ConfigurationProperties(MinidialogConfig.MINIDIALOG)
public class MinidialogConfig extends AbstractConfig {

    static final String MINIDIALOG = "minidialog";
    private static final String MINIDIALOG_DEV = MINIDIALOG + "/dev";
    private static final String MINIDIALOGER = MINIDIALOG_DEV + "/minidialoger";
    private static final String AKTIVE = MINIDIALOG_DEV + "/spm";

    private static final String DEFAULT_PING_PATH = "actuator/info";

    @ConstructorBinding
    public MinidialogConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    public URI minidialogPreprodURI(String fnr, boolean activeOnly) {
        return uri(getUri(), MINIDIALOGER, queryParams(Pair.of("fnr", fnr), Pair.of("activeOnly", activeOnly)));
    }

    public URI aktiveSpmURI() {
        return uri(getUri(), MINIDIALOG + "/me");
    }

    public URI aktiveSpmURI(String fnr) {
        return uri(getUri(), AKTIVE, queryParams(Pair.of("fnr", fnr)));
    }

    @Override
    public URI pingURI() {
        return uri(getUri(), DEFAULT_PING_PATH);
    }

}
