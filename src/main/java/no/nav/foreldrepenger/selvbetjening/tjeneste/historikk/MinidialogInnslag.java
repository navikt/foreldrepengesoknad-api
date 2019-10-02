package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

public class MinidialogInnslag extends HistorikkInnslag {

    private final HendelseType hendelse;
    private final LocalDate gyldigTil;
    private final String tekst;
    private Boolean aktiv;

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }

    @JsonCreator
    public MinidialogInnslag(@JsonProperty("fnr") Fødselsnummer fnr,
            @JsonProperty("hendelse") String hendelse, @JsonProperty("gyldigTil") LocalDate gyldigTil,
            @JsonProperty("journalpostId") String journalpostId, @JsonProperty("tekst") String tekst) {
        super(fnr);
        this.hendelse = hendelseFra(hendelse);
        this.gyldigTil = gyldigTil;
        this.tekst = tekst;
        super.setJournalpostId(journalpostId);
    }

    public String getTekst() {
        return tekst;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public HendelseType getHendelse() {
        return hendelse;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[hendelse=" + hendelse + ", gyldigTil=" + gyldigTil + ", journalpostId="
                + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + getOpprettet()
                + ",aktørId=" + getAktørId() + ", fnr=" + getFnr() + "]";
    }
}
