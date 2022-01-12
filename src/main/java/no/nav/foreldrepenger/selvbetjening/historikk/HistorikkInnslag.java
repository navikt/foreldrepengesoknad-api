package no.nav.foreldrepenger.selvbetjening.historikk;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static no.nav.foreldrepenger.selvbetjening.historikk.HendelseType.UKJENT;

import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.AktørId;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Fødselsnummer;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = InnsendingInnslag.class, name = "søknad"),
        @Type(value = InntektsmeldingInnslag.class, name = "inntekt"),
        @Type(value = MinidialogInnslag.class, name = "minidialog")
})
public abstract class HistorikkInnslag {

    private final Fødselsnummer fnr;
    private AktørId aktørId;
    private String journalpostId;
    private String saksnr;
    protected LocalDateTime opprettet;
    private String referanseId;

    protected HistorikkInnslag(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    protected HendelseType hendelseFra(String hendelse) {
        return Optional.ofNullable(hendelse)
                .map(HendelseType::tilHendelse)
                .orElse(UKJENT);
    }

    public String getSaksnr() {
        return saksnr;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public void setAktørId(AktørId aktørId) {
        this.aktørId = aktørId;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public void setReferanseId(String referanseId) {
        this.referanseId = referanseId;
    }
}
