package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.vedtak.uttak.Versjon;

public class VedtakMetadata {

    private final String journalpostId;
    private final Versjon versjon;

    @JsonCreator
    public VedtakMetadata(@JsonProperty("journalpostId") String journalpostId,
            @JsonProperty("versjon") Versjon versjon) {
        this.journalpostId = journalpostId;
        this.versjon = versjon;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public Versjon getVersjon() {
        return versjon;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [journalpostId=" + journalpostId + ", versjon=" + versjon + "]";
    }

}
