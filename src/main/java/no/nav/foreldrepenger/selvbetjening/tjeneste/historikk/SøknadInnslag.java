package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

public class SøknadInnslag extends HistorikkInnslag {

    private final HendelseType hendelse;
    private List<String> vedlegg;
    private LocalDate behandlingsdato;

    @JsonCreator
    public SøknadInnslag(@JsonProperty("fnr") Fødselsnummer fnr, @JsonProperty("hendelse") String hendelse) {
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
        return getClass().getSimpleName() + "[hendelse=" + hendelse + ", fnr=" + getFnr() + ", aktørId=" + getAktørId()
                + ", journalpostId=" + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + opprettet
                + ", vedlegg=" + vedlegg + ", behandlingsdato=" + behandlingsdato + "]";
    }

}
