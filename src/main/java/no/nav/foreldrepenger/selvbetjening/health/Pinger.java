package no.nav.foreldrepenger.selvbetjening.health;

import java.net.URI;

public interface Pinger {

    String ping(String message);

    URI baseUri();

}
