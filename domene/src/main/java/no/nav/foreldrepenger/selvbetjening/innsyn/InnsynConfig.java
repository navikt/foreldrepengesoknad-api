package no.nav.foreldrepenger.selvbetjening.innsyn;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.DokumentInfoId;
import no.nav.foreldrepenger.selvbetjening.innsyn.dokument.JournalpostId;
import no.nav.foreldrepenger.selvbetjening.util.URIUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

import static no.nav.foreldrepenger.selvbetjening.util.URIUtil.uri;

@ConfigurationProperties(prefix = "oversikt")
public class InnsynConfig {
    private static final String CONTEXT_PATH = "api";
    private static final String SAKER = CONTEXT_PATH + "/saker";
    private static final String ANNENPART_SAK = CONTEXT_PATH + "/annenPart/v2";
    private static final String ANNENPART_VEDTAK = CONTEXT_PATH + "/annenPart";
    private static final String OPPGAVER = CONTEXT_PATH + "/oppgaver";
    private static final String MANGLENDE_VEDLEGG = OPPGAVER + "/manglendevedlegg";
    private static final String INNTEKTSMELDINGER = CONTEXT_PATH + "/inntektsmeldinger";
    private static final String UTTALELSER_OM_TILBAKEKREVING = OPPGAVER + "/tilbakekrevingsuttalelse";
    private static final String ARBEID = CONTEXT_PATH + "/arbeid";
    private static final String MOR_DOKUMENTASJON_ARBEID_PATH = ARBEID + "/morDokumentasjon";
    private static final String TIDSLINJE = CONTEXT_PATH + "/tidslinje";
    private static final String DOKUMENT = CONTEXT_PATH + "/dokument";
    private static final String ALLE_DOKUMENTER = DOKUMENT + "/alle";

    private static final String QUERY_SAKSNUMMER = "saksnummer";
    private static final String QUERY_JOURNALPOSTID = "journalpostId";
    private static final String QUERY_DOKUMENTID = "dokumentId";

    private final URI baseUri;

    protected InnsynConfig(URI uri) {
        this.baseUri = uri;
    }

    URI saker() {
        return uri(getBaseUri(), SAKER);
    }

    URI manglendeOppgaver(Saksnummer saksnummer) {
        return uri(getBaseUri(), MANGLENDE_VEDLEGG, URIUtil.queryParam(QUERY_SAKSNUMMER, saksnummer.value()));
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
        return uri(getBaseUri(), INNTEKTSMELDINGER, URIUtil.queryParam(QUERY_SAKSNUMMER, saksnummer.value()));
    }

    URI tidlinje(Saksnummer saksnummer) {
        return uri(getBaseUri(), TIDSLINJE, URIUtil.queryParam(QUERY_SAKSNUMMER, saksnummer.value()));
    }

    URI hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return uri(getBaseUri(), DOKUMENT, URIUtil.queryParams(
                QUERY_JOURNALPOSTID, journalpostId.value(),
                QUERY_DOKUMENTID, dokumentId.value())
        );
    }

    URI dokumenter(Saksnummer saksnummer) {
        return uri(getBaseUri(), ALLE_DOKUMENTER, URIUtil.queryParam(QUERY_SAKSNUMMER, saksnummer.value()));
    }

    private URI getBaseUri() {
        return baseUri;
    }

}
