package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;

import no.nav.foreldrepenger.common.domain.felles.EttersendingsType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;

public final class EttersendingMapper {

    private EttersendingMapper() {
    }

    public static no.nav.foreldrepenger.common.domain.felles.Ettersending tilEttersending(EttersendelseDto ettersending) {
        return new no.nav.foreldrepenger.common.domain.felles.Ettersending(ettersending.saksnummer(),
            EttersendingsType.valueOf(ettersending.type().name()),
            tilVedlegg(ettersending.vedlegg()),
            ettersending.dialogId());
    }
}
