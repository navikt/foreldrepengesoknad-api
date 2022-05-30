package no.nav.foreldrepenger.selvbetjening.minidialog;

import static no.nav.foreldrepenger.common.util.Constants.FNR;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.util.URIUtil;

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
        return uri(getBaseUri(), MINIDIALOGER, queryParams(FNR, fnr, "activeOnly", String.valueOf(activeOnly)));
    }

    public URI aktiveSpmURI() {
        return uri(getBaseUri(), MINIDIALOG + "/me");
    }

    public URI aktiveSpmURI(String fnr) {
        return uri(getBaseUri(), AKTIVE, URIUtil.queryParam(FNR, fnr));
    }

    @Override
    public URI pingURI() {
        return uri(getBaseUri(), DEFAULT_PING_PATH);
    }

}
