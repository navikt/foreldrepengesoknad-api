package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

public class MinidialogInnslag {

    private long id;
    private AktørId aktørId;
    private final Fødselsnummer fnr;
    private boolean janei;
    private String vedlegg;
    private final String tekst;
    private final String saksnr;
    private LocalDateTime opprettet;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate gyldigTil;
    private Hendelse handling;

    @JsonCreator
    public MinidialogInnslag(@JsonProperty("fnr") Fødselsnummer fnr, @JsonProperty("tekst") String tekst,
            @JsonProperty("saksnr") String saksnr) {
        this.fnr = fnr;
        this.tekst = tekst;
        this.saksnr = saksnr;
    }

    public void setAktørId(AktørId aktørId) {
        this.aktørId = aktørId;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public void setGyldigTil(LocalDate gyldigTil) {
        this.gyldigTil = gyldigTil;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public String getTekst() {
        return tekst;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public boolean isJanei() {
        return janei;
    }

    public void setJanei(boolean janei) {
        this.janei = janei;
    }

    public String getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(String vedlegg) {
        this.vedlegg = vedlegg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Hendelse getHandling() {
        return handling;
    }

    public void setHandling(Hendelse handling) {
        this.handling = handling;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", fnr=" + fnr + ", janei=" + janei
                + ", vedlegg=" + vedlegg + ", tekst=" + tekst + ", saksnr=" + saksnr + ", opprettet=" + opprettet
                + ", endret=" + gyldigTil + ", handling=" + handling + "]";
    }

}
