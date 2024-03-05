package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.util.maler;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.PrivatArbeidsgiverDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.SelvstendigNæringsdrivendeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.VirksomhetDto;

public final class ArbeidsforholdMaler {

    private ArbeidsforholdMaler() {
    }

    public static VirksomhetDto virksomhet(Orgnummer orgnummer) {
        return new VirksomhetDto(orgnummer);
    }

    public static PrivatArbeidsgiverDto privatArbeidsgiver(Fødselsnummer fnr) {
        return new PrivatArbeidsgiverDto(fnr);
    }

    public static SelvstendigNæringsdrivendeDto selvstendigNæringsdrivende() {
        return new SelvstendigNæringsdrivendeDto(
            "risikofaktorer fra søker",
            "tilretteleggingstiltak fra søker"
        );
    }

}
