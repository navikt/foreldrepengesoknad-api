package no.nav.foreldrepenger.selvbetjening.innsending.domain.tilrettelegging;

import java.time.LocalDate;
import java.util.List;

public record Tilrettelegging(String type,
        Arbeidsforhold arbeidsforhold,
        LocalDate behovForTilretteleggingFom,
        LocalDate tilrettelagtArbeidFom,
        Double stillingsprosent,
        LocalDate slutteArbeidFom,
        List<String> vedlegg) {

}
