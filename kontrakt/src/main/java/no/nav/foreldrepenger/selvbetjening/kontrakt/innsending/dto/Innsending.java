package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggInnsendingType;


public interface Innsending {
    String navn();

    LocalDate mottattdato();

    List<VedleggDto> vedlegg();

    default List<VedleggDto> påkrevdeVedlegg() {
        return vedlegg().stream().filter(v -> !VedleggInnsendingType.AUTOMATISK.equals(v.innsendingsType())).toList();
    }
}
