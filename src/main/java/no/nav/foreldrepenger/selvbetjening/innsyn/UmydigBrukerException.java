package no.nav.foreldrepenger.selvbetjening.innsyn;

public class UmydigBrukerException extends RuntimeException {

    public UmydigBrukerException() {
        super("Bruker er under myndighetsalderen og har derfor ikke tilgang.");
    }
}
