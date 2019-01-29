package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public class Sak {

    private String type;

    private final String status;
    private final LocalDate opprettet;
    private final String saksnummer;
    private final List<Behandling> behandlinger;

    public Sak(String type, String saksnummer, String status, LocalDate opprettet, List<Behandling> behandlinger) {
        this.type = type;
        this.saksnummer = saksnummer;
        this.status = status;
        this.opprettet = opprettet;
        this.behandlinger = behandlinger = Optional.ofNullable(behandlinger).orElse(emptyList());
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

    public List<Behandling> getBehandlinger() {
        return behandlinger;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[type=" + type + ", saksnummer=" + saksnummer + ", status=" + status
                + ", opprettet=" + opprettet
                + ", behandlinger=" + behandlinger + "]";
    }
}
