package no.nav.foreldrepenger.selvbetjening.innsending.dto.endringssøknad;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.selvbetjening.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;

@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EndringssøknadForeldrepengerDto.class, name = "foreldrepenger")
})
public interface EndringssøknadDto {

    List<VedleggDto> vedlegg();

    default String type() {
        if (this instanceof EndringssøknadForeldrepengerDto) return "foreldrepenger";
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp!");
    }
}
