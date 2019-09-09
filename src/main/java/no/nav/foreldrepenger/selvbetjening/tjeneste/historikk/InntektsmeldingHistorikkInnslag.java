package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog.Hendelse.UKJENT;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.minidialog.Hendelse;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

public class InntektsmeldingHistorikkInnslag extends HistorikkInnslag {

    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingHistorikkInnslag.class);
    private Arbeidsgiver arbeidsgiver;

    @JsonCreator
    public InntektsmeldingHistorikkInnslag(@JsonProperty("fnr") Fødselsnummer fnr) {
        super(fnr);
    }

    private Hendelse hendelseFra(String hendelse) {
        return Optional.ofNullable(hendelse)
                .map(Hendelse::tilHendelse)
                .orElse(UKJENT);
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
