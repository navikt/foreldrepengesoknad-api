package no.nav.foreldrepenger.selvbetjening.http.interceptors;

import java.net.URI;

public interface ZoneCrossingAware {

    String getKey();

    URI zoneCrossingUri();

}
