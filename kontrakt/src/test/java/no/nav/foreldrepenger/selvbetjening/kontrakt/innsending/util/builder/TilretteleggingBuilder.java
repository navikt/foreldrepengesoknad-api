package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.builder;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.TilretteleggingDto;

public class TilretteleggingBuilder {
    private TilretteleggingDto.Type type;
    private ArbeidsforholdDto arbeidsforhold;
    private Double stillingsprosent;
    private LocalDate behovForTilretteleggingFom;
    private LocalDate tilrettelagtArbeidFom;
    private LocalDate slutteArbeidFom;
    private List<MutableVedleggReferanseDto> vedlegg;

    public static TilretteleggingBuilder hel(LocalDate behovForTilretteleggingFom, LocalDate tilrettelagtArbeidFom, ArbeidsforholdDto arbeidsforhold) {
        return new TilretteleggingBuilder(TilretteleggingDto.Type.HEL)
            .medBehovForTilretteleggingFom(behovForTilretteleggingFom)
            .medTilrettelagtArbeidFom(tilrettelagtArbeidFom)
            .medArbeidsforhold(arbeidsforhold);
    }
    public static TilretteleggingBuilder delvis(LocalDate behovForTilretteleggingFom, LocalDate tilrettelagtArbeidFom, ArbeidsforholdDto arbeidsforhold, Double stillingsprosent) {
        return new TilretteleggingBuilder(TilretteleggingDto.Type.DELVIS)
            .medBehovForTilretteleggingFom(behovForTilretteleggingFom)
            .medTilrettelagtArbeidFom(tilrettelagtArbeidFom)
            .medArbeidsforhold(arbeidsforhold)
            .medStillingsprosent(stillingsprosent);
    }
    public static TilretteleggingBuilder ingen(LocalDate behovForTilretteleggingFom, LocalDate slutteArbeidFom, ArbeidsforholdDto arbeidsforhold) {
        return new TilretteleggingBuilder(TilretteleggingDto.Type.INGEN)
            .medBehovForTilretteleggingFom(behovForTilretteleggingFom)
            .medSlutteArbeidFom(slutteArbeidFom)
            .medArbeidsforhold(arbeidsforhold);
    }

    private TilretteleggingBuilder(TilretteleggingDto.Type type) {
        this.type = type;
    }

    public TilretteleggingBuilder medType(TilretteleggingDto.Type type) {
        this.type = type;
        return this;
    }

    public TilretteleggingBuilder medArbeidsforhold(ArbeidsforholdDto arbeidsforhold) {
        this.arbeidsforhold = arbeidsforhold;
        return this;
    }

    public TilretteleggingBuilder medStillingsprosent(Double stillingsprosent) {
        this.stillingsprosent = stillingsprosent;
        return this;
    }

    public TilretteleggingBuilder medBehovForTilretteleggingFom(LocalDate behovForTilretteleggingFom) {
        this.behovForTilretteleggingFom = behovForTilretteleggingFom;
        return this;
    }

    public TilretteleggingBuilder medTilrettelagtArbeidFom(LocalDate tilrettelagtArbeidFom) {
        this.tilrettelagtArbeidFom = tilrettelagtArbeidFom;
        return this;
    }

    public TilretteleggingBuilder medSlutteArbeidFom(LocalDate slutteArbeidFom) {
        this.slutteArbeidFom = slutteArbeidFom;
        return this;
    }

    public TilretteleggingBuilder medVedlegg(List<MutableVedleggReferanseDto> vedlegg) {
        this.vedlegg = vedlegg;
        return this;
    }

    public TilretteleggingDto build() {
        return new TilretteleggingDto(
            type,
            arbeidsforhold,
            stillingsprosent,
            behovForTilretteleggingFom,
            tilrettelagtArbeidFom,
            slutteArbeidFom,
            vedlegg
        );
    }
}
