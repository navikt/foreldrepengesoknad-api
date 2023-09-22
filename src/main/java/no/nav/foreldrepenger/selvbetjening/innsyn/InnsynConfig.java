package no.nav.foreldrepenger.selvbetjening.innsyn;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.util.URIUtil;

@ConfigurationProperties(prefix = "oversikt")
public class InnsynConfig {
    private static final String CONTEXT_PATH = "api";
    private static final String SAKER = CONTEXT_PATH + "/saker";
    private static final String ANNENPART_VEDTAK = CONTEXT_PATH + "/annenPart";
    private static final String TIDSLINJE = CONTEXT_PATH + "/tidslinje";
    private static final String ALLE_DOKUMENTER = CONTEXT_PATH + "/arkiv/alle";

    private static final String OPPGAVER = CONTEXT_PATH + "/oppgaver";
    private static final String MANGLENDE_VEDLEGG = OPPGAVER + "/manglendevedlegg";
    private static final String UTTALELSER_OM_TILBAKEKREVING = OPPGAVER + "/tilbakekrevingsuttalelse";

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

    URI annenpartsVedtak() {
        return uri(getBaseUri(), ANNENPART_VEDTAK);
    }

    URI tidslinje(Saksnummer saksnummer) {
        return uri(getBaseUri(), TIDSLINJE, URIUtil.queryParam(SAKSNUMMER, saksnummer.value()));
    }

    URI alleDokumenter() {
        return uri(getBaseUri(), ALLE_DOKUMENTER);
    }

    URI alleDokumenter(Saksnummer saksnummer) {
        return uri(getBaseUri(), ALLE_DOKUMENTER, URIUtil.queryParam(SAKSNUMMER, saksnummer.value()));
    }

    private URI getBaseUri() {
        return baseUri;
    }


}
