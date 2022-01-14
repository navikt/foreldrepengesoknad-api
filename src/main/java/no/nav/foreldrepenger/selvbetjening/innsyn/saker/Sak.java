package no.nav.foreldrepenger.selvbetjening.innsyn.saker;

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

    private static final String TYPE = "FPSAK"; //TODO denne kan fjernes når frontend ikke bryr seg om type sak

    private final String status;
    private final LocalDate opprettet;
    private final String saksnummer;
    private final String fagsakId;
    private AnnenPart annenPart;
    private String behandlingTema;
    private final List<Behandling> behandlinger;
    private final boolean mottattEndringssøknad;

    @JsonCreator
    public Sak(@JsonProperty("saksnummer") String saksnummer,
               @JsonProperty("status") String status,
               @JsonProperty("opprettet") LocalDate opprettet,
               @JsonProperty("fagsakId") String fagsakId,
               @JsonProperty("annenPart") AnnenPart annenPart,
               @JsonProperty("behandlingTema") String behandlingTema,
               @JsonProperty("behandlinger") List<Behandling> behandlinger,
               @JsonProperty("mottattEndringssøknad") boolean mottattEndringssøknad) {
        this.saksnummer = saksnummer;
        this.status = status;
        this.opprettet = opprettet;
        this.fagsakId = fagsakId;
        this.annenPart = annenPart;
        this.behandlingTema = behandlingTema;
        this.behandlinger = Optional.ofNullable(behandlinger).orElse(emptyList());
        this.mottattEndringssøknad = mottattEndringssøknad;
    }

    public AnnenPart getAnnenPart() {
        return annenPart;
    }

    public void setAnnenPart(AnnenPart annenPart) {
        this.annenPart = annenPart;
    }

    public String getType() {
        return TYPE;
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

    public String getBehandlingTema() {
        return behandlingTema;
    }

    public boolean isMottattEndringssøknad() {
        return mottattEndringssøknad;
    }

    public void setBehandlingTema(String behandlingTema) {
        this.behandlingTema = behandlingTema;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[type=" + TYPE + ", status=" + status + ", opprettet=" + opprettet
            + ", saksnummer=" + saksnummer + ", fagsakId=" + fagsakId + ", mottattEndringssøknad=" + mottattEndringssøknad
            + ", annenPart=" + annenPart + ", behandlingTema=" + behandlingTema + ", behandlinger="
            + behandlinger + "]";
    }

}
