package no.nav.foreldrepenger.selvbetjening.innsyn.vedtak;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VedtakMetadata {

    private final String journalpostId;
    private final String versjon;

    @JsonCreator
    public VedtakMetadata(@JsonProperty("journalpostId") String journalpostId,
            @JsonProperty("versjon") String versjon) {
        this.journalpostId = journalpostId;
        this.versjon = versjon;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public String getVersjon() {
        return versjon;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [journalpostId=" + journalpostId + ", versjon=" + versjon + "]";
    }

}
