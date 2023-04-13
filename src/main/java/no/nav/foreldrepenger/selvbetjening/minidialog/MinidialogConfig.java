package no.nav.foreldrepenger.selvbetjening.minidialog;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import no.nav.foreldrepenger.selvbetjening.util.URIUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;

import static no.nav.foreldrepenger.common.util.Constants.FNR;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.queryParams;
import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties(MinidialogConfig.MINIDIALOG)
public class MinidialogConfig extends AbstractConfig {

    static final String MINIDIALOG = "minidialog";
    private static final String MINIDIALOG_DEV = MINIDIALOG + "/dev";
    private static final String MINIDIALOGER = MINIDIALOG_DEV + "/minidialoger";
    private static final String AKTIVE = MINIDIALOG_DEV + "/spm";

    private static final String DEFAULT_PING_PATH = "actuator/info";

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
