package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sak {

    private final String saksnummer;
    private final String status;
    private final String behandlingstema;
    private final String aktørId;
    private final String aktørIdAnnenPart;
    private final List<String> aktørIdBarn;
    private final List<Behandling> behandlinger;

    @JsonCreator
    public Sak(@JsonProperty("saksnummer") String saksnummer,
               @JsonProperty("fagsakStatus") String status,
               @JsonProperty("behandlingTema") String behandlingstema,
               @JsonProperty("aktørId") String aktørId,
               @JsonProperty("aktørIdAnnenPart") String aktørIdAnnenPart,
               @JsonProperty("aktørIdBarn") List<String> aktørIdBarn,
               @JsonProperty("behandlinger") List<Behandling> behandlinger) {
        this.saksnummer = saksnummer;
        this.status = status;
        this.behandlingstema = behandlingstema;
        this.aktørId = aktørId;
        this.aktørIdAnnenPart = aktørIdAnnenPart;
        this.aktørIdBarn = Optional.ofNullable(aktørIdBarn).orElse(emptyList());
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

    public String getBehandlingstema() {
        return behandlingstema;
    }

    public String getAktørId() {
        return aktørId;
    }

    public String getAktørIdAnnenPart() {
        return aktørIdAnnenPart;
    }

    public List<String> getAktørIdBarn() {
        return aktørIdBarn;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [saksnummer=" + saksnummer + ", status=" + status
                + ", behandlingstema="
                + behandlingstema + ", aktørId=" + aktørId + ", aktørIdAnnenPart=" + aktørIdAnnenPart + ", aktørIdBarn="
                + aktørIdBarn + ", behandlinger=" + behandlinger + "]";
    }

}
