package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import java.net.URI;

interface Vedlegg {

    byte[] hentVedlegg(URI uri);

    byte[] hentOgSlettVedlegg(URI url);

}
