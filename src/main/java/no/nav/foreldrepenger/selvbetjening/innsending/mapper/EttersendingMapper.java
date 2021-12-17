package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import java.util.ArrayList;

import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.EttersendingsType;
import no.nav.foreldrepenger.common.domain.felles.InnsendingsType;
import no.nav.foreldrepenger.common.domain.felles.PåkrevdVedlegg;
import no.nav.foreldrepenger.common.domain.felles.VedleggMetaData;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;

public final class EttersendingMapper {

    private EttersendingMapper() {
    }

    public static no.nav.foreldrepenger.common.domain.felles.Ettersending tilEttersending(Ettersending ettersending) {
        var ettersendingDto = new no.nav.foreldrepenger.common.domain.felles.Ettersending(
            ettersending.getType() != null ? EttersendingsType.valueOf(ettersending.getType()) : null,
            ettersending.getSaksnummer(),
            new ArrayList<>());
        ettersendingDto.setDialogId(ettersending.getDialogId());
        return ettersendingDto;
    }

    public static no.nav.foreldrepenger.common.domain.felles.Vedlegg tilVedlegg(Vedlegg vedlegg) {
        var vedleggMetadata = new VedleggMetaData(
            vedlegg.getBeskrivelse(),
            vedlegg.getId(),
            vedlegg.getInnsendingsType() != null ? InnsendingsType.valueOf(vedlegg.getInnsendingsType()) : null,
            vedlegg.getSkjemanummer() != null ? DokumentType.valueOf(vedlegg.getSkjemanummer()) : null);
        return new PåkrevdVedlegg(vedleggMetadata, vedlegg.getContent());
    }
}
