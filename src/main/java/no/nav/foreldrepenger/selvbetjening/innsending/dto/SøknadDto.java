package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.selvbetjening.innsending.dto.engangsstønad.EngangsstønadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger.Situasjon;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;

@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EngangsstønadDto.class, name = "engangsstønad"),
    @JsonSubTypes.Type(value = ForeldrepengesøknadDto.class, name = "foreldrepenger"),
    @JsonSubTypes.Type(value = SvangerskapspengesøknadDto.class, name = "svangerskapspenger")
})
public interface SøknadDto extends MottattTidspunkt {
    Situasjon situasjon();
    BarnDto barn();
    SøkerDto søker();
    UtenlandsoppholdDto informasjonOmUtenlandsopphold();
    List<VedleggDto> vedlegg();
    default String type() {
        if (this instanceof ForeldrepengesøknadDto) return "foreldrepenger";
        if (this instanceof EngangsstønadDto) return "engangsstønad";
        if (this instanceof SvangerskapspengesøknadDto) return "svangerskapspenger";
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp, svp eller es!");
    }
}
