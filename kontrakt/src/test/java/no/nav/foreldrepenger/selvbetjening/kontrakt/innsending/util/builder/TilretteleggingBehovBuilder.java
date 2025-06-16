package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.TilretteleggingbehovDto;


public class TilretteleggingBehovBuilder {
    private ArbeidsforholdDto arbeidsforhold;
    private LocalDate behovForTilretteleggingFom;
    private String risikofaktorer;
    private String tilretteleggingstiltak;
    private List<TilretteleggingbehovDto.TilretteleggingDto> tilrettelegginger = new ArrayList<>();

    public TilretteleggingBehovBuilder(ArbeidsforholdDto arbeidsforhold, LocalDate behovForTilretteleggingFom) {
        this.arbeidsforhold = arbeidsforhold;
        this.behovForTilretteleggingFom = behovForTilretteleggingFom;
        if (arbeidsforhold instanceof ArbeidsforholdDto.SelvstendigNæringsdrivendeDto || arbeidsforhold instanceof ArbeidsforholdDto.FrilanserDto) {
            this.risikofaktorer = "Risikofaktorer her";
            this.tilretteleggingstiltak = "Tilretteleggingstiltak her";
        }
    }

    public TilretteleggingBehovBuilder hel(LocalDate tilrettelagtArbeidFom) {
        this.tilrettelegginger.add(new TilretteleggingbehovDto.TilretteleggingDto.Hel(tilrettelagtArbeidFom));
        return this;
    }

    public TilretteleggingBehovBuilder delvis(LocalDate tilrettelagtArbeidFom, Double stillingsprosent) {
        this.tilrettelegginger.add(new TilretteleggingbehovDto.TilretteleggingDto.Del(tilrettelagtArbeidFom, stillingsprosent));
        return this;
    }

    public TilretteleggingBehovBuilder ingen(LocalDate slutteArbeidFom) {
        this.tilrettelegginger.add(new TilretteleggingbehovDto.TilretteleggingDto.Ingen(slutteArbeidFom));
        return this;
    }

    public TilretteleggingbehovDto build() {
        return new TilretteleggingbehovDto(arbeidsforhold, behovForTilretteleggingFom, risikofaktorer, tilretteleggingstiltak, tilrettelegginger);
    }
}
