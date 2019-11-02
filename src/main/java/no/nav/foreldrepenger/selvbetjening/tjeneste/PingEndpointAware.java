package no.nav.foreldrepenger.selvbetjening.tjeneste;

import java.net.URI;

public interface PingEndpointAware extends Pingable {

    URI pingURI();

    String name();

}
