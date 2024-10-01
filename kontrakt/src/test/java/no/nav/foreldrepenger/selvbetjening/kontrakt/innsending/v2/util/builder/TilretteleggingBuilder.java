package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.builder;

import java.time.LocalDate;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilrettelegging.DelvisTilretteleggingDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilrettelegging.HelTilretteleggingDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilrettelegging.IngenTilretteleggingDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilrettelegging.TilretteleggingDto;


public class TilretteleggingBuilder {

    public static HelTilretteleggingBuilder hel(LocalDate behovForTilretteleggingFom, LocalDate tilrettelagtArbeidFom, ArbeidsforholdDto arbeidsforhold) {
        return new HelTilretteleggingBuilder(behovForTilretteleggingFom)
            .medTilrettelagtArbeidFom(tilrettelagtArbeidFom)
            .medArbeidsforhold(arbeidsforhold);
    }
    public static DelvisTilretteleggingBuilder delvis(LocalDate behovForTilretteleggingFom, LocalDate tilrettelagtArbeidFom, ArbeidsforholdDto arbeidsforhold, Double stillingsprosent) {
        return new DelvisTilretteleggingBuilder(behovForTilretteleggingFom)
            .medTilrettelagtArbeidFom(tilrettelagtArbeidFom)
            .medArbeidsforhold(arbeidsforhold)
            .medStillingsprosent(stillingsprosent);
    }
    public static IngenTilretteleggingBuilder ingen(LocalDate behovForTilretteleggingFom, LocalDate slutteArbeidFom, ArbeidsforholdDto arbeidsforhold) {
        return new IngenTilretteleggingBuilder(behovForTilretteleggingFom)
            .medSlutteArbeidFom(slutteArbeidFom)
            .medArbeidsforhold(arbeidsforhold);
    }

    public static class HelTilretteleggingBuilder {
        private final LocalDate behovForTilretteleggingFom;
        private ArbeidsforholdDto arbeidsforhold;
        private LocalDate tilrettelagtArbeidFom;

        public HelTilretteleggingBuilder(LocalDate behovForTilretteleggingFom) {
            this.behovForTilretteleggingFom = behovForTilretteleggingFom;
        }

        public HelTilretteleggingBuilder medArbeidsforhold(ArbeidsforholdDto arbeidsforhold) {
            this.arbeidsforhold = arbeidsforhold;
            return this;
        }

        public HelTilretteleggingBuilder medTilrettelagtArbeidFom(LocalDate tilrettelagtArbeidFom) {
            this.tilrettelagtArbeidFom = tilrettelagtArbeidFom;
            return this;
        }

        public TilretteleggingDto build() {
            return new HelTilretteleggingDto(
                arbeidsforhold,
                behovForTilretteleggingFom,
                tilrettelagtArbeidFom
            );
        }
    }

    public static class DelvisTilretteleggingBuilder {
        private final LocalDate behovForTilretteleggingFom;
        private ArbeidsforholdDto arbeidsforhold;
        private LocalDate tilrettelagtArbeidFom;
        private Double stillingsprosent;

        public DelvisTilretteleggingBuilder(LocalDate behovForTilretteleggingFom) {
            this.behovForTilretteleggingFom = behovForTilretteleggingFom;
        }

        public DelvisTilretteleggingBuilder medArbeidsforhold(ArbeidsforholdDto arbeidsforhold) {
            this.arbeidsforhold = arbeidsforhold;
            return this;
        }

        public DelvisTilretteleggingBuilder medTilrettelagtArbeidFom(LocalDate tilrettelagtArbeidFom) {
            this.tilrettelagtArbeidFom = tilrettelagtArbeidFom;
            return this;
        }

        public DelvisTilretteleggingBuilder medStillingsprosent(Double stillingsprosent) {
            this.stillingsprosent = stillingsprosent;
            return this;
        }

        public TilretteleggingDto build() {
            return new DelvisTilretteleggingDto(
                arbeidsforhold,
                behovForTilretteleggingFom,
                tilrettelagtArbeidFom,
                stillingsprosent
            );
        }
    }

    public static class IngenTilretteleggingBuilder {
        private final LocalDate behovForTilretteleggingFom;
        private ArbeidsforholdDto arbeidsforhold;
        private LocalDate slutteArbeidFom;

        public IngenTilretteleggingBuilder(LocalDate behovForTilretteleggingFom) {
            this.behovForTilretteleggingFom = behovForTilretteleggingFom;
        }

        public IngenTilretteleggingBuilder medArbeidsforhold(ArbeidsforholdDto arbeidsforhold) {
            this.arbeidsforhold = arbeidsforhold;
            return this;
        }

        public IngenTilretteleggingBuilder medSlutteArbeidFom(LocalDate slutteArbeidFom) {
            this.slutteArbeidFom = slutteArbeidFom;
            return this;
        }

        public TilretteleggingDto build() {
            return new IngenTilretteleggingDto(
                arbeidsforhold,
                behovForTilretteleggingFom,
                slutteArbeidFom
            );
        }
    }
}
