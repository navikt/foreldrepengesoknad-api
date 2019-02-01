package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Behandling {

    private final LocalDateTime opprettetTidspunkt;
    private final LocalDateTime endretTidspunkt;
    private final String status;
    private final String type;
    private final String tema;
    private final String årsak;
    private final BehandlingResultat behandlingResultat;
    private final String behandlendeEnhet;
    private final String behandlendeEnhetNavn;
    private List<String> inntektsmeldinger;

    @JsonCreator
    public Behandling(
            @JsonProperty("opprettetTidspunkt") LocalDateTime opprettetTidspunkt,
            @JsonProperty("endretTidspunkt") LocalDateTime endretTidspunkt, @JsonProperty("status") String status,
            @JsonProperty("type") String type,
            @JsonProperty("tema") String tema,
            @JsonProperty("årsak") String årsak,
            @JsonProperty("behandlingResultat") BehandlingResultat behandlingResultat,
            @JsonProperty("behandlendeEnhet") String behandlendeEnhet,
            @JsonProperty("behandlendeEnhetNavn") String behandlendeEnhetNavn,
            @JsonProperty("inntektsmeldinger") List<String> inntektsmeldinger) {
        this.opprettetTidspunkt = opprettetTidspunkt;
        this.endretTidspunkt = endretTidspunkt;
        this.status = status;
        this.type = type;
        this.tema = tema;
        this.årsak = årsak;
        this.behandlingResultat = behandlingResultat;
        this.behandlendeEnhet = behandlendeEnhet;
        this.behandlendeEnhetNavn = behandlendeEnhetNavn;
        this.inntektsmeldinger = inntektsmeldinger;
    }

    public List<String> getInntektsmeldinger() {
        return inntektsmeldinger;
    }

    public void setInntektsmeldinger(List<String> inntektsmeldinger) {
        this.inntektsmeldinger = inntektsmeldinger;
    }

    public BehandlingResultat getBehandlingResultat() {
        return behandlingResultat;
    }

    public LocalDateTime getOpprettetTidspunkt() {
        return opprettetTidspunkt;
    }

    public LocalDateTime getEndretTidspunkt() {
        return endretTidspunkt;
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
        return getClass().getSimpleName() + " [opprettetTidspunkt=" + opprettetTidspunkt + ", endretTidspunkt="
                + endretTidspunkt
                + ", status=" + status + ", type=" + type + ", tema=" + tema + ", årsak=" + årsak
                + ", behandlingResultat=" + behandlingResultat + ", behandlendeEnhet=" + behandlendeEnhet
                + ", behandlendeEnhetNavn=" + behandlendeEnhetNavn + ", inntektsmeldinger=" + inntektsmeldinger + "]";
    }

}
