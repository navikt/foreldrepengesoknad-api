package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
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
