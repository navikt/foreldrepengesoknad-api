package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending;

import java.net.URI;

interface Vedlegg {

    byte[] hentVedlegg(URI url);

    byte[] hentOgSlettVedlegg(URI url);

    void slettVedlegg(URI url);
}
