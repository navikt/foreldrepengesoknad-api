package no.nav.foreldrepenger.selvbetjening.innsending.domain;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.FRITEKST;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import no.nav.foreldrepenger.common.domain.Saksnummer;

public record EttersendingFrontend(@Pattern(regexp = FRITEKST) @NotNull String type,
                                   @Valid Saksnummer saksnummer,
                                   @Valid @Size(max = 20) List<VedleggFrontend> vedlegg,
                                   @Valid BrukerTekst brukerTekst,
                                   @Pattern(regexp = FRITEKST) String dialogId) {

    public EttersendingFrontend(String type, Saksnummer saksnummer, List<VedleggFrontend> vedlegg, BrukerTekst brukerTekst, String dialogId) {
        this.type = type;
        this.saksnummer = saksnummer;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
        this.brukerTekst = brukerTekst;
        this.dialogId = dialogId;
    }
}
