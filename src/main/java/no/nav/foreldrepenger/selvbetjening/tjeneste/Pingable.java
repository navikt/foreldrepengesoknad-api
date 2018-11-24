package no.nav.foreldrepenger.selvbetjening.tjeneste;

import java.net.URI;

public interface Pingable {
    String ping();

    URI pingEndpoint();

    String name();
}
