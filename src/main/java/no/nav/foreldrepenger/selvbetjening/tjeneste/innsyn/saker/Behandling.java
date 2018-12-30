package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Behandling {

    private final LocalDateTime opprettetTidspunkt;
    private final LocalDateTime endretTidspunkt;
    private final String status;
    private final String type;
    private final String tema;
    private final String årsak;
    private final BehandlingResultatType behandlingResultatType;
    private final String behandlendeEnhet;
    private final String behandlendeEnhetNavn;

    @JsonCreator
    public Behandling(
            @JsonProperty("opprettetTidspunkt") LocalDateTime opprettetTidspunkt,
            @JsonProperty("endretTidspunkt") LocalDateTime endretTidspunkt, @JsonProperty("status") String status,
            @JsonProperty("type") String type,
            @JsonProperty("tema") String tema,
            @JsonProperty("årsak") String årsak,
            @JsonProperty("behandlingResultatType") BehandlingResultatType behandlingResultatType,
            @JsonProperty("behandlendeEnhet") String behandlendeEnhet,
            @JsonProperty("behandlendeEnhetNavn") String behandlendeEnhetNavn) {
        this.opprettetTidspunkt = opprettetTidspunkt;
        this.endretTidspunkt = endretTidspunkt;
        this.status = status;
        this.type = type;
        this.tema = tema;
        this.årsak = årsak;
        this.behandlingResultatType = behandlingResultatType;
        this.behandlendeEnhet = behandlendeEnhet;
        this.behandlendeEnhetNavn = behandlendeEnhetNavn;
    }

    public BehandlingResultatType getBehandlingResultatType() {
        return behandlingResultatType;
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
                + ", behandlingResultatType=" + behandlingResultatType + ", behandlendeEnhet=" + behandlendeEnhet
                + ", behandlendeEnhetNavn=" + behandlendeEnhetNavn + "]";
    }

}
