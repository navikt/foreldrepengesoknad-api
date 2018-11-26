package no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.dto;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsending.domain.Vedlegg;

public class EttersendingDto {
    public String saksnr;
    public List<VedleggDto> vedlegg = new ArrayList<>();

    public EttersendingDto(Ettersending ettersending) {
        this.saksnr =  ettersending.saksnummer;
    }

    public void addVedlegg(Vedlegg vedlegg) {
        VedleggDto vedleggDto = new VedleggDto();
        vedleggDto.type = "påkrevd";
        vedleggDto.metadata.id = vedlegg.id;
        vedleggDto.metadata.dokumentType = vedlegg.skjemanummer;
        vedleggDto.vedlegg = vedlegg.content;
        this.vedlegg.add(vedleggDto);
    }
}
