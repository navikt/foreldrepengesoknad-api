package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.http.AbstractConfig;

@ConfigurationProperties(prefix = "innsyn")
public class InnsynConfig extends AbstractConfig {

    private static final String PING = "actuator/health/liveness";
    private static final String FPSAK_SAKER = "innsyn/saker";

    private static final String ARBEIDSFORHOLD = "innsyn/arbeidsforhold";
    private static final String SAKSNUMMER = "saksnummer";
    private static final String ANNENPART = "annenPart";
    private static final String UTTAKSPLAN = "innsyn/uttaksplan";
    private static final String UTTAKSPLANANNEN = "innsyn/uttaksplanannen";

    private static final String FPSAK_SAKER_V2 = "innsyn/v2/saker";

    @ConstructorBinding
    public InnsynConfig(URI uri, @DefaultValue("true") boolean enabled) {
        super(uri, enabled);
    }

    @Override
    public URI pingURI() {
        return uri(getUri(), PING);
    }

    URI fpsakURI() {
        return uri(getUri(), FPSAK_SAKER);
    }

    URI uttakURI(String saksnummer) {
        return uri(getUri(), UTTAKSPLAN, queryParams(SAKSNUMMER, saksnummer));
    }

    URI uttakURIForAnnenPart(Fødselsnummer annenPart) {
        return uri(getUri(), UTTAKSPLANANNEN, queryParams(ANNENPART, annenPart.value()));
    }

    URI fpsakV2URI() {
        return uri(getUri(), FPSAK_SAKER_V2);
    }

    public URI arbeidsforholdURI() {
        return uri(getUri(), ARBEIDSFORHOLD);
    }
}
