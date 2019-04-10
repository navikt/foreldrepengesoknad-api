package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.tilrettelegging;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_EMPTY)
public class Tilrettelegging {

    public String type;
    public Arbeidsforhold arbeidsforhold;
    public LocalDate behovForTilretteleggingFom;
    public LocalDate tilrettelagtArbeidFom;
    public Double stillingsprosent;
    public LocalDate slutteArbeidFom;
    public List<String> vedlegg;
}
