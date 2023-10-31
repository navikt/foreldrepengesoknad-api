package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import java.time.LocalDate;
import java.util.List;


public interface Innsending {
    String navn();
    LocalDate mottattdato();
    List<VedleggDto> vedlegg();
}
