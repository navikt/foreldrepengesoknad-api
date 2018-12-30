package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public class Sak {

    public Sak() {
    }

    public Sak(String type, String saksnummer, String status, LocalDate opprettet, List<Behandling> behandlinger) {
        this.type = type;
        this.saksnummer = saksnummer;
        this.status = status;
        this.opprettet = opprettet;
        this.behandlinger = behandlinger;
    }

    public String type;
    public String saksnummer;
    public String status;
    public LocalDate opprettet;
    public List<Behandling> behandlinger;
}
