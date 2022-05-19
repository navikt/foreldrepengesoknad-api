package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import java.util.ArrayList;

import no.nav.foreldrepenger.common.domain.felles.EttersendingsType;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;

public final class EttersendingMapper {

    private EttersendingMapper() {
    }

    public static no.nav.foreldrepenger.common.domain.felles.Ettersending tilEttersending(EttersendingFrontend ettersending) {
        return new no.nav.foreldrepenger.common.domain.felles.Ettersending(
            ettersending.saksnummer(),
            EttersendingsType.valueOf(ettersending.type()),
            new ArrayList<>(),
            ettersending.dialogId());
    }
}
