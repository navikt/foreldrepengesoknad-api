package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

public class InntektsmeldingInnslag extends HistorikkInnslag {

    private Arbeidsgiver arbeidsgiver;

    @JsonCreator
    public InntektsmeldingInnslag(@JsonProperty("fnr") Fødselsnummer fnr) {
        super(fnr);
    }

    public Arbeidsgiver getArbeidsgiver() {
        return arbeidsgiver;
    }

    public void setArbeidsgiver(Arbeidsgiver arbeidsgiver) {
        this.arbeidsgiver = arbeidsgiver;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fnr=" + getFnr() + ", aktørId=" + getAktørId() + ", journalpostId="
                + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + opprettet + ", arbeidsgiver="
                + arbeidsgiver
                + "]";
    }

}
