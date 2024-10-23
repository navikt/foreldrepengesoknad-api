package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.util.maler;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.ArbeidsforholdDto;

public final class ArbeidsforholdMaler {

    private ArbeidsforholdMaler() {
    }

    public static ArbeidsforholdDto virksomhet(Orgnummer orgnummer) {
        return new ArbeidsforholdDto.VirksomhetDto(orgnummer);
    }

    public static ArbeidsforholdDto privatArbeidsgiver(Fødselsnummer fnr) {
        return new ArbeidsforholdDto.PrivatArbeidsgiverDto(fnr);
    }

    public static ArbeidsforholdDto selvstendigNæringsdrivende() {
        return new ArbeidsforholdDto.SelvstendigNæringsdrivendeDto();
    }

}
