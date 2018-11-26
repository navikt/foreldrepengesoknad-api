package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;

@JsonInclude(NON_EMPTY)
public class SøknadDto {

    public LocalDateTime mottattdato;
    public SøkerDto søker;
    public List<VedleggDto> vedlegg = new ArrayList<>();

    public void addVedlegg(Vedlegg vedlegg) {
        VedleggDto vedleggDto = new VedleggDto();
        vedleggDto.type = "påkrevd";
        vedleggDto.metadata.id = vedlegg.id;
        vedleggDto.metadata.dokumentType = vedlegg.skjemanummer;
        vedleggDto.metadata.innsendingsType = vedlegg.innsendingsType;
        vedleggDto.vedlegg = vedlegg.content;
        this.vedlegg.add(vedleggDto);
    }
}
