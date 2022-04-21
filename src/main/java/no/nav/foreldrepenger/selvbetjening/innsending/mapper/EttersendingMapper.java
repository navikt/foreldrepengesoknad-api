package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import java.util.ArrayList;

import no.nav.foreldrepenger.common.domain.felles.EttersendingsType;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;

public final class EttersendingMapper {

    private EttersendingMapper() {
    }

    public static no.nav.foreldrepenger.common.domain.felles.Ettersending tilEttersending(EttersendingFrontend ettersending) {
        var ettersendingDto = new no.nav.foreldrepenger.common.domain.felles.Ettersending(
            ettersending.type() != null ? EttersendingsType.valueOf(ettersending.type()) : null,
            ettersending.saksnummer(),
            new ArrayList<>());
        ettersendingDto.setDialogId(ettersending.dialogId());
        return ettersendingDto;
    }
}
