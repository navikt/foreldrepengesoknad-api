package no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public record Tilrettelegging(String type,
                              Arbeidsforhold arbeidsforhold,
                              Double stillingsprosent,
                              LocalDate behovForTilretteleggingFom,
                              LocalDate tilrettelagtArbeidFom,
                              LocalDate slutteArbeidFom,
                              List<String> vedlegg) {

    @JsonCreator
    public Tilrettelegging(String type, Arbeidsforhold arbeidsforhold, Double stillingsprosent,
                           LocalDate behovForTilretteleggingFom, LocalDate tilrettelagtArbeidFom,
                           LocalDate slutteArbeidFom, List<String> vedlegg) {
        this.type = type;
        this.arbeidsforhold = arbeidsforhold;
        this.stillingsprosent = stillingsprosent;
        this.behovForTilretteleggingFom = behovForTilretteleggingFom;
        this.tilrettelagtArbeidFom = tilrettelagtArbeidFom;
        this.slutteArbeidFom = slutteArbeidFom;
        this.vedlegg = Optional.ofNullable(vedlegg).orElse(emptyList());
    }
}
