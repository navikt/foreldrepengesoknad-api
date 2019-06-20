package no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MinidialogInnslag {

    private final String aktørId;
    private final String melding;
    private final String saksnr;
    private LocalDateTime opprettet;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate gyldigTil;
    private SøknadType handling;

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

    public String getAktørId() {
        return aktørId;
    }

    public String getMelding() {
        return melding;
    }

    public String getSaksnr() {
        return saksnr;
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[aktørId=" + aktørId + ", melding=" + melding + ", saksnr=" + saksnr
                + ", opprettet=" + opprettet + ", gyldigTil=" + gyldigTil + ", handling=" + handling + "]";
    }

}
