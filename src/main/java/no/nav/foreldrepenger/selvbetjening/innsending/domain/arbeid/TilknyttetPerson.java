package no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid;

public class TilknyttetPerson {

    private String navn;
    private String telefonnummer;
    private Boolean erNærVennEllerFamilie;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[navn=" + getNavn() + ", telefonnummer=" + getTelefonnummer()
                + ", erNærVennEllerFamilie="
                + getErNærVennEllerFamilie() + "]";
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public Boolean getErNærVennEllerFamilie() {
        return erNærVennEllerFamilie;
    }

    public void setErNærVennEllerFamilie(Boolean erNærVennEllerFamilie) {
        this.erNærVennEllerFamilie = erNærVennEllerFamilie;
    }
}
