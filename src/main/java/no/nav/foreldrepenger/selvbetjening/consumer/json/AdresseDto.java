package no.nav.foreldrepenger.selvbetjening.consumer.json;

public class AdresseDto {

    public String gatenavn;
    public String postnummer;
    public String poststed;
    public String bolignummer;
    public String husbokstav;
    public String landkode;

    public String adresse() {
        return String.format("%s %s%s, %s %s", gatenavn, bolignummer, husbokstav, postnummer, poststed);
    }
}
