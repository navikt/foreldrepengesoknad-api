package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak.Uttak;

public class Vedtak {
    private final VedtakMetadata metadata;
    private final Uttak uttak;

    @JsonCreator
    public Vedtak(@JsonProperty("metadata") VedtakMetadata metadata, @JsonProperty("uttak") Uttak uttak) {
        this.metadata = metadata;
        this.uttak = uttak;
    }

    public VedtakMetadata getMetadata() {
        return metadata;
    }

    public Uttak getUttak() {
        return uttak;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [metadata=" + metadata + ", uttak=" + uttak + "]";
    }

}
