package no.nav.foreldrepenger.selvbetjening.felles.health;

import java.net.URI;

public interface Pinger {

    String ping(String message);

    URI baseUri();

}
