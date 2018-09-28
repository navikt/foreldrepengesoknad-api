package no.nav.foreldrepenger.selvbetjening.oppslag.json;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sak {

    private final String saksnummer;
    private final String status;
    private final List<Behandling> behandlinger;

    @JsonCreator
    public Sak(@JsonProperty("saksnummer") String saksnummer,
               @JsonProperty("status") String status,
               @JsonProperty("behandlinger") List<Behandling> behandlinger) {
        this.saksnummer = saksnummer;
        this.status = status;
        this.behandlinger = Optional.ofNullable(behandlinger).orElse(emptyList());
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
