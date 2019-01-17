package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
class VedleggTjeneste implements Vedlegg {
    private final RestOperations operations;

    public VedleggTjeneste(RestOperations operations) {
        this.operations = operations;
    }

    @Override
    public byte[] hentVedlegg(URI uri) {
        return operations.getForObject(uri, byte[].class);
    }

    @Override
    public byte[] hentOgSlettVedlegg(URI uri) {
        byte[] vedlegg = hentVedlegg(uri);
        operations.delete(uri);
        return vedlegg;

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [operations=" + operations + "]";
    }
}
