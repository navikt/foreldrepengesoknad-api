package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.SituasjonOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = EndringssøknadForeldrepengerDtoOLD.class, name = "foreldrepenger")})
public interface EndringssøknadDtoOLD extends Innsending {
    SituasjonOLD situasjon();

    Saksnummer saksnummer();

    BarnDtoOLD barn();

    SøkerDtoOLD søker();

    List<VedleggDto> vedlegg();

    default String navn() {
        if (this instanceof EndringssøknadForeldrepengerDtoOLD) {
            return "endringssøknad foreldrepenger";
        }
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp!");
    }
}
