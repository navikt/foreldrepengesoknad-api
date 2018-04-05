package no.nav.foreldrepenger.selvbetjening.consumer.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.nav.foreldrepenger.selvbetjening.rest.json.Engangsstønad;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

public class EngangsstønadDto {

    public LocalDateTime mottattdato;

    public SøkerDto søker;
    public YtelseDto ytelse;
    @JsonInclude(NON_EMPTY)
    public List<VedleggDto> vedlegg;

    public EngangsstønadDto() {}

    public EngangsstønadDto(Engangsstønad engangsstønad, PersonDto person) {
        this.søker = new SøkerDto();
        this.ytelse = new YtelseDto(engangsstønad.utenlandsopphold, engangsstønad.barn, engangsstønad.annenForelder);
        this.vedlegg = new ArrayList<>();

        // TODO: Mottak bør hente personinfo via fnr fra oidc token selv.
        this.søker.fornavn = person.fornavn;
        this.søker.etternavn = person.etternavn;
        this.søker.fnr = person.fnr;
        this.søker.aktør = person.aktorId;

        this.søker.søknadsRolle = "MOR";

        this.mottattdato = LocalDateTime.now();
    }

    public void addVedlegg(byte[] vedlegg) {
        VedleggDto vedleggDto = new VedleggDto();
        vedleggDto.type = "påkrevd";
        vedleggDto.metadata.beskrivelse = "Terminbekreftelse";
        vedleggDto.metadata.type = "PDF";
        vedleggDto.metadata.skjemanummer = "TERMINBEKREFTELSE";
        vedleggDto.vedlegg = vedlegg;
        this.vedlegg.add(vedleggDto);
    }
}
