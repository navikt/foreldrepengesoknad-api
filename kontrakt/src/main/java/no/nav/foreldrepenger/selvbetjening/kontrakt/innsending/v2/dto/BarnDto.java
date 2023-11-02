package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;

@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = FødselDto.class, name = "fødsel"),
    @JsonSubTypes.Type(value = TerminDto.class, name = "termin"),
    @JsonSubTypes.Type(value = AdopsjonDto.class, name = "adopsjon"),
    @JsonSubTypes.Type(value = OmsorgsovertakelseDto.class, name = "omsorgsovertakelse")
})
public interface BarnDto {
    int antallBarn();
    List<MutableVedleggReferanseDto> vedleggreferanser();
}
