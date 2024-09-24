package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.endringssøknad;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;

@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EndringssøknadForeldrepengerDto.class, name = "foreldrepenger")
})
public interface EndringssøknadDto extends Innsending {
    Saksnummer saksnummer();
    BarnDto barn();
    List<VedleggDto> vedlegg();

    default String navn() {
        if (this instanceof EndringssøknadForeldrepengerDto) return "endringssøknad foreldrepenger";
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp!");
    }
}
