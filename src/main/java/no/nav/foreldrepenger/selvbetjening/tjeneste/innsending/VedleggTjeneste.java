package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
class VedleggTjeneste implements Vedlegg {

    private static final Logger LOG = LoggerFactory.getLogger(VedleggTjeneste.class);
    private final RestOperations operations;

    public VedleggTjeneste(RestOperations operations) {
        this.operations = operations;
    }

    @Override
    public byte[] hentVedlegg(URI url) {
        LOG.info("Henter vedlegg fra {}", url);
        return operations.getForObject(url, byte[].class);
    }

    @Override
    public byte[] hentOgSlettVedlegg(URI url) {
        byte[] vedlegg = hentVedlegg(url);
        operations.delete(url);
        LOG.info("Sletter vedlegg på  {}", url);
        return vedlegg;
    }

    @Override
    public void slettVedlegg(URI url) {
        LOG.info("Sletter vedlegg på  {}", url);
        operations.delete(url);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [operations=" + operations + "]";
    }
}
