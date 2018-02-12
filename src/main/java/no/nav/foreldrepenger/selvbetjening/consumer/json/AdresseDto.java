package no.nav.foreldrepenger.selvbetjening.consumer.json;

import static java.util.Optional.ofNullable;

public class AdresseDto {

    public String gatenavn;
    public String postnummer;
    public String poststed;
    public String bolignummer;
    public String husbokstav;
    public String landkode;

    public String adresse() {
        return String.format("%s %s%s, %s %s", gatenavn, bolignummer, emptyIfNull(husbokstav), postnummer, poststed);
    }

    private String emptyIfNull(String value) {
        return ofNullable(value).orElse("");
    }
}
