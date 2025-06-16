package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum VedleggInnsendingType {
    @JsonEnumDefaultValue LASTET_OPP,
    SEND_SENERE,
    AUTOMATISK
}
