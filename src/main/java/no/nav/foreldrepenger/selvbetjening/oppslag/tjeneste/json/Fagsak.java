package no.nav.foreldrepenger.selvbetjening.oppslag.tjeneste.json;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Fagsak {

    private final String saksnummer;
    private final String fagsakStatus;
    private final String behandlingTema;
    private final String aktørId;
    private final String aktørIdAnnenPart;
    private final List<String> aktørIdBarn;
    private final List<Behandling> behandlinger;

    @JsonCreator
    public Fagsak(@JsonProperty("saksnummer") String saksnummer,
            @JsonProperty("fagsakStatus") String fagsakStatus,
            @JsonProperty("behandlingTema") String behandlingsTema,
            @JsonProperty("aktørId") String aktørId,
            @JsonProperty("aktørIdAnnenPart") String aktørIdAnnenPart,
            @JsonProperty("aktørIdBarn") List<String> aktørIdBarn,
            @JsonProperty("behandlinger") List<Behandling> behandlinger) {
        this.saksnummer = saksnummer;
        this.fagsakStatus = fagsakStatus;
        this.behandlingTema = behandlingsTema;
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

    public String getFagsakStatus() {
        return fagsakStatus;
    }

    public String getBehandlingTema() {
        return behandlingTema;
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
        return getClass().getSimpleName() + " [saksnummer=" + saksnummer + ", fagsakStatus=" + fagsakStatus
                + ", behandlingTema="
                + behandlingTema + ", aktørId=" + aktørId + ", aktørIdAnnenPart=" + aktørIdAnnenPart + ", aktørIdBarn="
                + aktørIdBarn + ", behandlinger=" + behandlinger + "]";
    }

}
