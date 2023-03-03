package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties(prefix = "innsyn")
public class InnsynConfig extends AbstractConfig {

    private static final String PING = "actuator/health/liveness";
    private static final String ARBEIDSFORHOLD = "innsyn/arbeidsforhold";
    private static final String SAKER = "innsyn/v2/saker";
    private static final String ANNEN_PART_VEDTAK = "innsyn/v2/annenPartVedtak";

    @ConstructorBinding
    public InnsynConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    public URI pingURI() {
        return uri(getBaseUri(), PING);
    }

    URI sakerURI() {
        return uri(getBaseUri(), SAKER);
    }

    URI annenPartVedtakURI() {
        return uri(getBaseUri(), ANNEN_PART_VEDTAK);
    }

    public URI arbeidsforholdURI() {
        return uri(getBaseUri(), ARBEIDSFORHOLD);
    }
}
