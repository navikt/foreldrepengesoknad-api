package no.nav.foreldrepenger.selvbetjening.service;

import java.net.URI;

public interface Pinger {

    String ping(String message);

    URI baseUri();

}
