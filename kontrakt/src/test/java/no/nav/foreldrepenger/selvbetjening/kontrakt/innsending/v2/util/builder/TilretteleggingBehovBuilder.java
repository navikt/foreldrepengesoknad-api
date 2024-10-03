package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilretteleggingbehov.TilretteleggingbehovDto;


public class TilretteleggingBehovBuilder {

    public static TilretteleggingbehovDto.HelTilretteleggingDto hel(LocalDate tilrettelagtArbeidFom) {
        return new TilretteleggingbehovDto.HelTilretteleggingDto(tilrettelagtArbeidFom);
    }
    public static TilretteleggingbehovDto.DelvisTilretteleggingDto delvis(LocalDate tilrettelagtArbeidFom, Double stillingsprosent) {
        return new TilretteleggingbehovDto.DelvisTilretteleggingDto(tilrettelagtArbeidFom, stillingsprosent);
    }
    public static TilretteleggingbehovDto.IngenTilretteleggingDto ingen(LocalDate slutteArbeidFom) {
        return new TilretteleggingbehovDto.IngenTilretteleggingDto(slutteArbeidFom);
    }

}
