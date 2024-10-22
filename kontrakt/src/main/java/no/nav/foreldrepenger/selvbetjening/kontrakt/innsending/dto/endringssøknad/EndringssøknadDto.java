package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Situasjon;

@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EndringssøknadForeldrepengerDto.class, name = "foreldrepenger")
})
public interface EndringssøknadDto extends Innsending {
    Situasjon situasjon();
    Saksnummer saksnummer();
    BarnDto barn();
    SøkerDto søker();
    List<VedleggDto> vedlegg();

    default String navn() {
        if (this instanceof EndringssøknadForeldrepengerDto) return "endringssøknad foreldrepenger";
        throw new IllegalStateException("Utvikerfeil: Kan ikke ha en annen ytelse enn fp!");
    }
}
