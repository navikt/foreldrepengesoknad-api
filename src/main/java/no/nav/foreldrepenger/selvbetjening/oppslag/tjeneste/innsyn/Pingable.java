package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.innsyn;

import java.net.URI;

public interface Pingable {
    String ping();

    URI pingEndpoint();

    String name();
}
