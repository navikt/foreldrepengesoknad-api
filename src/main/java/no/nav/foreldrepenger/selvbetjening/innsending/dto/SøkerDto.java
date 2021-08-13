package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public record SøkerDto(String søknadsRolle, String språkKode) {

    public SøkerDto(String rolle) {
        this(rolle, null);
    }

    public SøkerDto(String søknadsRolle, String språkKode) {
        this.søknadsRolle = rolle(søknadsRolle);
        this.språkKode = språkKode;
    }

    private static String rolle(String rolle) {
        return (morFarEllerMedmor(rolle)) ? rolle : "ANDRE";
    }

    private static boolean morFarEllerMedmor(String rolle) {
        return (rolle != null) && (rolle.equals("MOR") || rolle.equals("FAR") || rolle.equals("MEDMOR"));
    }

}
