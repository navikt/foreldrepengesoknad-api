package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(NON_EMPTY)
public class Sak {

    private String type;

    private final String status;
    private final LocalDate opprettet;
    private final String saksnummer;
    private final String fagsakId;
    private AnnenPart annenPart;
    private final List<Behandling> behandlinger;

    @JsonCreator
    public Sak(@JsonProperty("type") String type,
            @JsonProperty("saksnummer") String saksnummer,
            @JsonProperty("status") String status,
            @JsonProperty("opprettet") LocalDate opprettet,
            @JsonProperty("fagsakId") String fagsakId,
            @JsonProperty("annenPart") AnnenPart annenPart,
            @JsonProperty("behandlinger") List<Behandling> behandlinger) {
        this.type = type;
        this.saksnummer = saksnummer;
        this.status = status;
        this.opprettet = opprettet;
        this.fagsakId = fagsakId;
        this.annenPart = annenPart;
        this.behandlinger = Optional.ofNullable(behandlinger).orElse(emptyList());
    }

    public AnnenPart getAnnenPart() {
        return annenPart;
    }

    public void setAnnenPart(AnnenPart annenPart) {
        this.annenPart = annenPart;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getOpprettet() {
        return opprettet;
    }

    public String getFagsakId() {
        return fagsakId;
    }

    public List<Behandling> getBehandlinger() {
        return behandlinger;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [type=" + type + ", status=" + status + ", opprettet=" + opprettet
                + ", saksnummer=" + saksnummer
                + ", fagsakId=" + fagsakId + ", annenPart=" + annenPart + ", behandlinger=" + behandlinger + "]";
    }

}
