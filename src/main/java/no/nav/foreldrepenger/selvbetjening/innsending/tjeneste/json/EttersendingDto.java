package no.nav.foreldrepenger.selvbetjening.innsending.tjeneste.json;

import no.nav.foreldrepenger.selvbetjening.innsending.json.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.json.Vedlegg;

import java.util.ArrayList;
import java.util.List;

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
        vedleggDto.metadata.beskrivelse = vedlegg.type;
        vedleggDto.metadata.dokumentType = vedlegg.skjemanummer;
        vedleggDto.vedlegg = vedlegg.content;
        this.vedlegg.add(vedleggDto);
    }
}
