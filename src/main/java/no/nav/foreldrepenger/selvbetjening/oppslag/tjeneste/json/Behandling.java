package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json;

public class Behandling {

    private final String status;
    private final String type;
    private final String tema;
    private final String årsak;
    private final String behandlendeEnhet;
    private final String behandlendeEnhetNavn;

    public Behandling(String status, String type, String tema, String årsak, String behandlendeEnhet,
            String behandlendeEnhetNavn) {
        this.status = status;
        this.type = type;
        this.tema = tema;
        this.årsak = årsak;
        this.behandlendeEnhet = behandlendeEnhet;
        this.behandlendeEnhetNavn = behandlendeEnhetNavn;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getTema() {
        return tema;
    }

    public String getÅrsak() {
        return årsak;
    }

    public String getBehandlendeEnhet() {
        return behandlendeEnhet;
    }

    public String getBehandlendeEnhetNavn() {
        return behandlendeEnhetNavn;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [status=" + status + ", type=" + type + ", tema=" + tema + ", årsak="
                + årsak
                + ", behandlendeEnhet=" + behandlendeEnhet + ", behandlendeEnhetNavn=" + behandlendeEnhetNavn + "]";
    }

}
