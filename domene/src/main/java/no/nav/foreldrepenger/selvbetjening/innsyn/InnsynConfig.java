package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.util.URIUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties(prefix = "oversikt")
public class InnsynConfig {
    private static final String SAKER = "api/saker";
    private static final String ANNENPART_SAK = "api/annenPart/v2";
    private static final String ANNENPART_VEDTAK = "api/annenPart";
    private static final String MANGLENDE_VEDLEGG = "api/oppgaver/manglendevedlegg";
    private static final String UTTALELSER_OM_TILBAKEKREVING = "api/oppgaver/tilbakekrevingsuttalelse";
    private static final String INNTEKTSMELDINGER = "api/inntektsmeldinger";
    private static final String MOR_DOKUMENTASJON_ARBEID_PATH = "api/arbeid/morDokumentasjon";

    private static final String SAKSNUMMER = "saksnummer";

    private final URI baseUri;

    protected InnsynConfig(URI uri) {
        this.baseUri = uri;
    }

    URI saker() {
        return uri(getBaseUri(), SAKER);
    }

    URI manglendeOppgaver(Saksnummer saksnummer) {
        return uri(getBaseUri(), MANGLENDE_VEDLEGG, URIUtil.queryParam(SAKSNUMMER, saksnummer.value()));
    }

    URI uttalelseOmTilbakekrevinger() {
        return uri(getBaseUri(), UTTALELSER_OM_TILBAKEKREVING);
    }

    URI annenpartsSak() {
        return uri(getBaseUri(), ANNENPART_SAK);
    }

    URI annenpartsVedtak() {
        return uri(getBaseUri(), ANNENPART_VEDTAK);
    }

    URI trengerDokumentereMorsArbeid() {
        return uri(getBaseUri(), MOR_DOKUMENTASJON_ARBEID_PATH);
    }

    URI inntektsmelding(Saksnummer saksnummer) {
        return uri(getBaseUri(), INNTEKTSMELDINGER, URIUtil.queryParam(SAKSNUMMER, saksnummer.value()));
    }

    private URI getBaseUri() {
        return baseUri;
    }
}
