package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDtoOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.SituasjonOLD;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;

@Deprecated
@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = ForeldrepengesøknadDtoOLD.class, name = "foreldrepenger"),})
public interface SøknadDtoOLD extends Innsending {
    SituasjonOLD situasjon();

    BarnDtoOLD barn();

    SøkerDtoOLD søker();

    UtenlandsoppholdDtoOLD informasjonOmUtenlandsopphold();

    List<VedleggDto> vedlegg();

    default String navn() {
        if (this instanceof ForeldrepengesøknadDtoOLD) {
            return "foreldrepenger";
        }
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp, svp eller es!");
    }
}
