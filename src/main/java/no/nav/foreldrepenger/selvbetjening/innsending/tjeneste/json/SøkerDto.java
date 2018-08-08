package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

public class SøkerDto {

    public String søknadsRolle;

    public SøkerDto(String rolle) {
        if (morFarEllerMedmor(rolle)) {
            this.søknadsRolle = rolle;
        } else {
            this.søknadsRolle = "ANDRE";
        }
    }

    private boolean morFarEllerMedmor(String rolle) {
        return rolle != null && (rolle.equals("MOR") || rolle.equals("FAR") || rolle.equals("MEDMOR"));
    }

}
