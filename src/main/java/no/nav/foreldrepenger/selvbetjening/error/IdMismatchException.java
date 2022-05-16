package no.nav.foreldrepenger.selvbetjening.error;

public class IdMismatchException extends RuntimeException {

    private final String headerFnr;
    private final String authenticatedUser;

    public IdMismatchException(String headerFnr, String authenticatedUser) {
        super("Fødselsnummer i søknad matcher ikke innlogget bruker. Forekommer typisk når søker og annenpart søker på samme maskin.");
        this.headerFnr = headerFnr;
        this.authenticatedUser = authenticatedUser;
    }

    public String getHeaderFnr() {
        return headerFnr;
    }

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

}
