package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MinidialogInnslag {

    private long id;
    private final String aktørId;
    private final String melding;
    private final String saksnr;
    private LocalDateTime opprettet;
    private LocalDateTime endret;
    private LeveranseKanal kanal;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate gyldigTil;
    private SøknadType handling;
    private boolean aktiv;

    @JsonCreator
    public MinidialogInnslag(@JsonProperty("aktørId") String aktørId, @JsonProperty("melding") String melding,
            @JsonProperty("saksnr") String saksnr) {
        this.aktørId = aktørId;
        this.melding = melding;
        this.saksnr = saksnr;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public void setGyldigTil(LocalDate gyldigTil) {
        this.gyldigTil = gyldigTil;
    }

    public LeveranseKanal getKanal() {
        return kanal;
    }

    public void setKanal(LeveranseKanal kanal) {
        this.kanal = kanal;
    }

    public String getAktørId() {
        return aktørId;
    }

    public String getMelding() {
        return melding;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public SøknadType getHandling() {
        return handling;
    }

    public void setHandling(SøknadType handling) {
        this.handling = handling;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public LocalDateTime getEndret() {
        return endret;
    }

    public void setEndret(LocalDateTime endret) {
        this.endret = endret;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", melding=" + melding + ", saknr="
                + saksnr + ", opprettet=" + opprettet + ", endret=" + endret + ", kanal=" + kanal + ", gyldigTil="
                + gyldigTil + ", handling=" + handling + ", aktiv=" + aktiv + "]";
    }

}
