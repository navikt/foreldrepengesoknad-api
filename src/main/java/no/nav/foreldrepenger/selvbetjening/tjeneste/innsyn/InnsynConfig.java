package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "innsyn")
@Configuration
public class InnsynConfig {

    private static final String DEFAULT_BASE_PATH = "/api";
    private static final String DEFAULT_PING_PATH = DEFAULT_BASE_PATH + "/mottak/ping";

    static final String SAKSNUMMER = "saksnummer";
    static final String UTTAKSPLAN = "/innsyn/uttaksplan";

    String pingPath;
    boolean enabled;
    URI mottakURI;
    URI oppslagURI;
    String basePath;

    public InnsynConfig(@Value("${FPSOKNAD_MOTTAK_API_URL}") URI mottakURI,
            @Value("${FPSOKNAD_OPPSLAG_API_URL}") URI oppslagURI) {
        this.mottakURI = mottakURI;
        this.oppslagURI = oppslagURI;
    }

    public URI getOppslagURI() {
        return oppslagURI;
    }

    public void setOppslagURI(URI oppslagURI) {
        this.oppslagURI = oppslagURI;
    }

    public String getBasePath() {
        return Optional.ofNullable(basePath).orElse(DEFAULT_BASE_PATH);
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public URI getMottakURI() {
        return mottakURI;
    }

    public void setMottakURI(URI mottakURI) {
        this.mottakURI = mottakURI;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPingPath() {
        return Optional.ofNullable(pingPath).orElse(DEFAULT_PING_PATH);
    }

    public void setPingPath(String pingPath) {
        this.pingPath = pingPath;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingPath=" + pingPath + ", enabled=" + enabled + ", uri=" + mottakURI
                + ", basePath=" + basePath
                + "]";
    }

}
