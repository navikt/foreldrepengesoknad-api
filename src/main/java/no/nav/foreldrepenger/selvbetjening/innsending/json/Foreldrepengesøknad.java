package no.nav.foreldrepenger.selvbetjening.innsending.json;

public class Foreldrepengesøknad extends Søknad {

    public Barn barn;
    public AnnenForelder annenForelder;
    public Utenlandsopphold utenlandsopphold;

    public String situasjon;

    public Foreldrepengesøknad() {
        this.barn = new Barn();
        this.annenForelder = new AnnenForelder();
        this.utenlandsopphold = new Utenlandsopphold();
    }

}
