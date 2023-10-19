package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler;

import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;

public final class ArbeidsforholdMaler {

    private ArbeidsforholdMaler() {
    }

    public static ArbeidsforholdDto virksomhet(Orgnummer orgnummer) {
        return new ArbeidsforholdDto(
                ArbeidsforholdDto.Type.VIRKSOMHET,
                orgnummer.value(),
                null,
                null
                );
    }

    public static ArbeidsforholdDto privatArbeidsgiver(String fnr) {
        return new ArbeidsforholdDto(
                ArbeidsforholdDto.Type.PRIVAT,
                fnr,
                null,
                null
        );
    }

    public static ArbeidsforholdDto selvstendigNæringsdrivende() {
        return new ArbeidsforholdDto(
                ArbeidsforholdDto.Type.SELVSTENDIG,
                null,
                "risikofaktorer",
                "tilretteleggingstiltak"
        );
    }

}
