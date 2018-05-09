package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class SøknadDto {

    public LocalDateTime mottattdato;
    public SøkerDto søker;
    public List<VedleggDto> vedlegg;

    public void addVedlegg(byte[] vedlegg) {
        VedleggDto vedleggDto = new VedleggDto();
        vedleggDto.type = "påkrevd";
        vedleggDto.metadata.beskrivelse = "Terminbekreftelse";
        vedleggDto.metadata.skjemanummer = "TERMINBEKREFTELSE";
        vedleggDto.vedlegg = vedlegg;
        this.vedlegg.add(vedleggDto);
    }
}
