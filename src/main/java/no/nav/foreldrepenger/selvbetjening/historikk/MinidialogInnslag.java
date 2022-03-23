package no.nav.foreldrepenger.selvbetjening.historikk;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;

public class MinidialogInnslag extends HistorikkInnslag {

    private final HendelseType hendelse;
    private final LocalDate gyldigTil;
    private final String tekst;
    private final String dialogId;
    private Boolean aktiv;

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }

    @JsonCreator
    public MinidialogInnslag(@JsonProperty("fnr") Fødselsnummer fnr,
            @JsonProperty("hendelse") String hendelse,
            @JsonProperty("gyldigTil") LocalDate gyldigTil,
            @JsonProperty("journalpostId") String journalpostId,
            @JsonProperty("tekst") String tekst,
            @JsonProperty("referanseId") String referanseId,
            @JsonProperty("dialogId") String dialogId) {

        super(fnr);
        this.hendelse = hendelseFra(hendelse);
        this.gyldigTil = gyldigTil;
        this.tekst = tekst;
        this.dialogId = dialogId;
        super.setJournalpostId(journalpostId);
        super.setReferanseId(referanseId);
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

    public String getDialogId() {
        return dialogId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[hendelse=" + hendelse + ", gyldigTil=" + gyldigTil + ", journalpostId="
                + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + getOpprettet()
                + ",aktørId=" + getAktørId() + ", fnr=" + getFnr() + ", referanseId=" + getReferanseId() + ", dialogId="
                + dialogId + "]";
    }

}
