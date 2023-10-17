package no.nav.foreldrepenger.selvbetjening.innsending.domain.endringssøknad;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.BarnDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.MottattTidspunkt;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.foreldrepenger.Situasjon;

@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EndringssøknadForeldrepengerDto.class, name = "foreldrepenger")
})
public interface EndringssøknadDto extends MottattTidspunkt {
    Situasjon situasjon();
    BarnDto barn();
    SøkerDto søker();
    List<VedleggDto> vedlegg();

    default String type() {
        if (this instanceof EndringssøknadForeldrepengerDto) return "foreldrepenger";
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp!");
    }
}
