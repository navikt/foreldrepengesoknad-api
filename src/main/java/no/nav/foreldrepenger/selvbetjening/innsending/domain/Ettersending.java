package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

public record Ettersending(String type,
                           String saksnummer,
                           List<VedleggFrontend> vedlegg,
                           BrukerTekst brukerTekst,
                           String dialogId) {

    public Ettersending(String type, String saksnummer, List<VedleggFrontend> vedlegg, BrukerTekst brukerTekst, String dialogId) {
        this.type = type;
        this.saksnummer = saksnummer;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
        this.brukerTekst = brukerTekst;
        this.dialogId = dialogId;
    }
}
