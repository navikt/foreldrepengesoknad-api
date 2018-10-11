package no.nav.foreldrepenger.selvbetjening.oppslag.json;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sak {

    private final String saksnummer;
    private final String fagsystemSakId;
    private final String status;
    private final String behandlingTema;
    private final LocalDate opprettet;
    private final List<Behandling> behandlinger;

    @JsonCreator
    public Sak(@JsonProperty("saksnummer") String saksnummer,
            @JsonProperty("fagsystemSakId") String fagsystemSakId,
            @JsonProperty("status") String status, @JsonProperty("behandlingTema") String behandlingTema,
            @JsonProperty("opprettet") LocalDate opprettet,
            @JsonProperty("behandlinger") List<Behandling> behandlinger) {
        this.saksnummer = saksnummer;
        this.fagsystemSakId = fagsystemSakId;
        this.status = status;
        this.behandlingTema = behandlingTema;
        this.opprettet = opprettet;
        this.behandlinger = Optional.ofNullable(behandlinger).orElse(emptyList());
    }

    public String getFagsystemSakId() {
        return fagsystemSakId;
    }

    public LocalDate getOpprettet() {
        return opprettet;
    }

    public String getBehandlingTema() {
        return behandlingTema;
    }

    public List<Behandling> getBehandlinger() {
        return behandlinger;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Sak{" +
                "saksnummer='" + saksnummer + '\'' +
                ", fagsystemSakId='" + fagsystemSakId + '\'' +
                ", status='" + status + '\'' +
                ", behandlingTema='" + behandlingTema + '\'' +
                ", behandlinger=" + behandlinger +
                '}';
    }
}
