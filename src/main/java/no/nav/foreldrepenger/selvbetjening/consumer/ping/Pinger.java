package no.nav.foreldrepenger.selvbetjening.consumer.ping;

import java.net.URI;

public interface Pinger {

    String ping(String message);

    URI baseUri();

}
