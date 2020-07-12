package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;

@JsonInclude(NON_EMPTY)
public class SøknadDto {

    public LocalDate mottattdato;
    public SøkerDto søker;
    public List<VedleggDto> vedlegg = new ArrayList<>();
    public String tilleggsopplysninger;

    public void addVedlegg(Vedlegg vedlegg) {
        VedleggDto vedleggDto = new VedleggDto();
        vedleggDto.type = "påkrevd";
        vedleggDto.metadata.id = vedlegg.getId();
        vedleggDto.metadata.dokumentType = vedlegg.getSkjemanummer();
        vedleggDto.metadata.innsendingsType = vedlegg.getInnsendingsType();
        vedleggDto.metadata.beskrivelse = vedlegg.getBeskrivelse();
        vedleggDto.vedlegg = vedlegg.getContent();
        this.vedlegg.add(vedleggDto);
    }
}
