package no.nav.foreldrepenger.selvbetjening.http;

import java.net.URI;

public interface PingEndpointAware extends Pingable {

    URI pingURI();

    String name();

    boolean isEnabled();

}
