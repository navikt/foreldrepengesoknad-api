package no.nav.foreldrepenger.selvbetjening.innsending;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.selvbetjening.historikk.HendelseType;
import no.nav.foreldrepenger.selvbetjening.historikk.HistorikkInnslag;

public class InnsendingInnslag extends HistorikkInnslag {

    private final HendelseType hendelse;
    private List<String> vedlegg;
    private List<String> opplastedeVedlegg;
    private List<String> ikkeOpplastedeVedlegg;

    public List<String> getOpplastedeVedlegg() {
        return opplastedeVedlegg;
    }

    public void setOpplastedeVedlegg(List<String> opplastedeVedlegg) {
        this.opplastedeVedlegg = opplastedeVedlegg;
    }

    public List<String> getIkkeOpplastedeVedlegg() {
        return ikkeOpplastedeVedlegg;
    }

    public void setIkkeOpplastedeVedlegg(List<String> ikkeOpplastedeVedlegg) {
        this.ikkeOpplastedeVedlegg = ikkeOpplastedeVedlegg;
    }

    private LocalDate behandlingsdato;

    @JsonCreator
    public InnsendingInnslag(@JsonProperty("fnr") Fødselsnummer fnr, @JsonProperty("hendelse") String hendelse) {
        super(fnr);
        this.hendelse = hendelseFra(hendelse);
    }

    public LocalDate getBehandlingsdato() {
        return behandlingsdato;
    }

    public void setBehandlingsdato(LocalDate behandlingsdato) {
        this.behandlingsdato = behandlingsdato;
    }

    public List<String> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<String> vedlegg) {
        this.vedlegg = vedlegg;
    }

    public HendelseType getHendelse() {
        return hendelse;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [hendelse=" + hendelse + ", vedlegg=" + vedlegg + ", opplastedeVedlegg=" + opplastedeVedlegg
                + ", ikkeOpplastedeVedlegg=" + ikkeOpplastedeVedlegg + ", behandlingsdato=" + behandlingsdato + "]";
    }
}
