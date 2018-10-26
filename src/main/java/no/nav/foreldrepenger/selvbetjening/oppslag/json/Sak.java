package no.nav.foreldrepenger.selvbetjening.oppslag.json;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sak {

    private final String saksnummer;
    private final String status;
    private final LocalDate opprettet;
    private final List<Behandling> behandlinger;

    @JsonCreator
    public Sak(@JsonProperty("saksnummer") String saksnummer,
            @JsonProperty("status") String status,
            @JsonProperty("opprettet") LocalDate opprettet,
            @JsonProperty("behandlinger") List<Behandling> behandlinger) {
        this.saksnummer = saksnummer;
        this.status = status;
        this.opprettet = opprettet;
        this.behandlinger = Optional.ofNullable(behandlinger).orElse(emptyList());
    }

    public LocalDate getOpprettet() {
        return opprettet;
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
}
