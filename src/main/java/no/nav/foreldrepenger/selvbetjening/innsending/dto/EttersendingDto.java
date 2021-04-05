package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;

public class EttersendingDto {
    public String type;
    public String saksnr;
    public List<VedleggDto> vedlegg = new ArrayList<>();
    public String dialogId;

    public EttersendingDto(Ettersending ettersending) {
        this.type = ettersending.getType();
        this.saksnr = ettersending.getSaksnummer();
        this.dialogId = ettersending.getDialogId();
    }

    public void addVedlegg(Vedlegg vedlegg) {
        this.vedlegg.add(new VedleggDto("påkrevd",
                new VedleggMetadataDto(vedlegg.getId(), vedlegg.getSkjemanummer(), vedlegg.getInnsendingsType(), vedlegg.getBeskrivelse()),
                vedlegg.getContent()));
    }
}
