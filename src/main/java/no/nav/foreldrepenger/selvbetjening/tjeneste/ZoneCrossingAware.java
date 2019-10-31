package no.nav.foreldrepenger.selvbetjening.tjeneste;

import java.net.URI;

public interface ZoneCrossingAware {

    String getKey();

    URI zoneCrossingUri();

}
