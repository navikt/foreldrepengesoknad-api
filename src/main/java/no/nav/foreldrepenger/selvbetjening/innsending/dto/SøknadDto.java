package no.nav.foreldrepenger.selvbetjening.innsending.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;

public class SøknadDto {

    public LocalDate mottattdato;
    public SøkerDto søker;
    public List<VedleggDto> vedlegg = new ArrayList<>();
    public String tilleggsopplysninger;

    public void addVedlegg(Vedlegg vedlegg) {
        this.vedlegg.add(new VedleggDto("påkrevd",
                new VedleggMetadataDto(vedlegg.getId(), vedlegg.getSkjemanummer(), vedlegg.getInnsendingsType(), vedlegg.getBeskrivelse()),
                vedlegg.getContent()));
    }
}
