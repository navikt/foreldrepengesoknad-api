package no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.MutableVedleggReferanse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_BOKSTAVER;

@JsonInclude(NON_EMPTY)
public record Tilrettelegging(@Pattern(regexp = BARE_BOKSTAVER) String type,
                              @Valid Arbeidsforhold arbeidsforhold,
                              Double stillingsprosent,
                              LocalDate behovForTilretteleggingFom,
                              LocalDate tilrettelagtArbeidFom,
                              LocalDate slutteArbeidFom,
                              List<MutableVedleggReferanse> vedlegg) {

    @JsonCreator
    public Tilrettelegging(String type, Arbeidsforhold arbeidsforhold, Double stillingsprosent,
                           LocalDate behovForTilretteleggingFom, LocalDate tilrettelagtArbeidFom,
                           LocalDate slutteArbeidFom, List<MutableVedleggReferanse> vedlegg) {
        this.type = type;
        this.arbeidsforhold = arbeidsforhold;
        this.stillingsprosent = stillingsprosent;
        this.behovForTilretteleggingFom = behovForTilretteleggingFom;
        this.tilrettelagtArbeidFom = tilrettelagtArbeidFom;
        this.slutteArbeidFom = slutteArbeidFom;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
    }
}
