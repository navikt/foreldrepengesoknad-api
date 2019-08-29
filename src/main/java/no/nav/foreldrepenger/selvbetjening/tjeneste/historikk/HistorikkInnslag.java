package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog.Hendelse;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

public class HistorikkInnslag {

    private AktørId aktørId;
    private Fødselsnummer fnr;
    private String journalpostId;
    private Hendelse hendelse;
    private LocalDateTime opprettet;
    private String saksnr;
    List<String> vedlegg;

    @JsonCreator
    public HistorikkInnslag(@JsonProperty("aktørId") AktørId aktørId, @JsonProperty("hendelse") Hendelse hendelse) {
        this.aktørId = aktørId;
        this.hendelse = hendelse;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public void setFnr(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public void setAktørId(AktørId aktørId) {
        this.aktørId = aktørId;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    public Hendelse getHendelse() {
        return hendelse;
    }

    public void setHendelse(Hendelse hendelse) {
        this.hendelse = hendelse;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    public List<String> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<String> vedlegg) {
        this.vedlegg = vedlegg;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[aktørId=" + aktørId + ", journalpostId=" + journalpostId + ", hendelse="
                + hendelse + ", opprettet=" + opprettet + ", saksnr=" + saksnr + "]";
    }

}
