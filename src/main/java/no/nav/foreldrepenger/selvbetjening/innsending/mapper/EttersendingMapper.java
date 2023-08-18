package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import no.nav.foreldrepenger.common.domain.felles.EttersendingsType;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilVedlegg;

public final class EttersendingMapper {

    private EttersendingMapper() {
    }

    public static no.nav.foreldrepenger.common.domain.felles.Ettersending tilEttersending(EttersendingFrontend ettersending) {
        return new no.nav.foreldrepenger.common.domain.felles.Ettersending(
            ettersending.saksnummer(),
            EttersendingsType.valueOf(ettersending.type().toUpperCase()),
            tilVedlegg(ettersending.vedlegg()),
            ettersending.dialogId());
    }
}
