package no.nav.foreldrepenger.selvbetjening.innsyn.dokument;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestOperations;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperationRequest;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequest;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResponseProjection;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResult;

import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.safselvbetjening.Dokumentoversikt;
import no.nav.safselvbetjening.DokumentoversiktResponseProjection;
import no.nav.safselvbetjening.DokumentoversiktSelvbetjeningQueryRequest;
import no.nav.safselvbetjening.DokumentoversiktSelvbetjeningQueryResponse;


@Component
public class SafSelvbetjeningConnection extends AbstractRestConnection {

    private final SafSelvbetjeningConfig cfg;
    protected SafSelvbetjeningConnection(RestOperations operations, SafSelvbetjeningConfig cfg) {
        super(operations);
        this.cfg = cfg;
    }

    public ResponseEntity<byte[]> hentDokument(JournalpostId journalpostId, DokumentInfoId dokumentId) {
        return getForEntity(cfg.hentDokument().toUriString(), byte[].class, journalpostId.value(), dokumentId.value());
    }

    public Dokumentoversikt dokumentoversiktSelvbetjening(DokumentoversiktSelvbetjeningQueryRequest q, DokumentoversiktResponseProjection p) {
        return query(q, p, DokumentoversiktSelvbetjeningQueryResponse.class).dokumentoversiktSelvbetjening();
    }

    private <T extends GraphQLResult<?>> T query(GraphQLOperationRequest req, GraphQLResponseProjection p, Class<T> clazz) {
        return query(new GraphQLRequest(req, p), clazz);
    }

    private <T extends GraphQLResult<?>> T query(GraphQLRequest req, Class<T> clazz) {
        var respons = postForObject(cfg.graphqlPath(), req.toHttpJsonBody(), clazz);
        if (respons.hasErrors()) {
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500), "Feil mot interne systemer."); // TODO
        }
        return respons;
    }

}
