package no.nav.foreldrepenger.selvbetjening.interceptors.client;

import java.net.URI;

public interface ZoneCrossingAware {

    String getKey();

    URI zoneCrossingUri();

}
