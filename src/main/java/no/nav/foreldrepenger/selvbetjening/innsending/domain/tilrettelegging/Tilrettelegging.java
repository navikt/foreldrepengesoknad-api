package no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.common.domain.felles.VedleggReferanse;

@JsonInclude(NON_EMPTY)
public record Tilrettelegging(@Pattern(regexp = BARE_BOKSTAVER) String type,
                              @Valid Arbeidsforhold arbeidsforhold,
                              Double stillingsprosent,
                              LocalDate behovForTilretteleggingFom,
                              LocalDate tilrettelagtArbeidFom,
                              LocalDate slutteArbeidFom,
                              List<VedleggReferanse> vedlegg) {

    @JsonCreator
    public Tilrettelegging(String type, Arbeidsforhold arbeidsforhold, Double stillingsprosent,
                           LocalDate behovForTilretteleggingFom, LocalDate tilrettelagtArbeidFom,
                           LocalDate slutteArbeidFom, List<VedleggReferanse> vedlegg) {
        this.type = type;
        this.arbeidsforhold = arbeidsforhold;
        this.stillingsprosent = stillingsprosent;
        this.behovForTilretteleggingFom = behovForTilretteleggingFom;
        this.tilrettelagtArbeidFom = tilrettelagtArbeidFom;
        this.slutteArbeidFom = slutteArbeidFom;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
    }
}
