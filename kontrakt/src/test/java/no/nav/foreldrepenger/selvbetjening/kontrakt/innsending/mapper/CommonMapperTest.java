package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.common.domain.felles.DokumentType.I000041;
import static no.nav.foreldrepenger.common.domain.felles.InnsendingsType.LASTET_OPP;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.Orgnummer;
import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.VedleggMetaData;
import no.nav.foreldrepenger.common.domain.svangerskapspenger.tilrettelegging.arbeidsforhold.Virksomhet;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.ArbeidsforholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ÅpenPeriodeDto;

class CommonMapperTest {

    private static final DokumentType DOKUMENTTYPE = I000041;

    @Test
    void vedleggSomDokumentererUttak() {
        var vedleggDto = vedlegg(uttak());
        var vedleggMappet  = CommonMapper.tilVedlegg(vedleggDto);

        assertThat(vedleggMappet.getId()).isEqualTo(vedleggDto.getUuid());
        assertThat(vedleggMappet.getMetadata().innsendingsType()).isEqualTo(LASTET_OPP);
        assertThat(vedleggMappet.getDokumentType()).isEqualTo(DOKUMENTTYPE);
        assertThat(vedleggMappet.getMetadata().hvaDokumentererVedlegg().type()).isEqualTo(VedleggMetaData.Dokumenterer.Type.UTTAK);
        assertThat(vedleggMappet.getMetadata().hvaDokumentererVedlegg().perioder()).hasSize(1);
    }

    @Test
    void vedleggSomDokumentererTilrettelegging() {
        var vedleggDto = vedlegg(tilrettelegging());
        var vedleggMappet  = CommonMapper.tilVedlegg(vedleggDto);

        assertThat(vedleggMappet.getId()).isEqualTo(vedleggDto.getUuid());
        assertThat(vedleggMappet.getMetadata().innsendingsType()).isEqualTo(LASTET_OPP);
        assertThat(vedleggMappet.getDokumentType()).isEqualTo(DOKUMENTTYPE);
        assertThat(vedleggMappet.getMetadata().hvaDokumentererVedlegg().type()).isEqualTo(VedleggMetaData.Dokumenterer.Type.TILRETTELEGGING);
        assertThat(vedleggMappet.getMetadata().hvaDokumentererVedlegg().arbeidsforhold()).isNotNull();
        assertThat(vedleggMappet.getMetadata().hvaDokumentererVedlegg().arbeidsforhold()).isInstanceOf(Virksomhet.class);
    }

    @Test
    void vedleggSomDokumentererAnnet() {
        var vedleggDto = vedlegg(annet());
        var vedleggMappet  = CommonMapper.tilVedlegg(vedleggDto);

        assertThat(vedleggMappet.getId()).isEqualTo(vedleggDto.getUuid());
        assertThat(vedleggMappet.getMetadata().innsendingsType()).isEqualTo(LASTET_OPP);
        assertThat(vedleggMappet.getDokumentType()).isEqualTo(DOKUMENTTYPE);
        assertThat(vedleggMappet.getMetadata().hvaDokumentererVedlegg()).isNull();
    }



    private static VedleggDto vedlegg(VedleggDto.Dokumenterer dokumenterer) {
        return new VedleggDto(
            null,
            "Beskrivelse",
            "LASTET_OPP",
            DOKUMENTTYPE.name(),
            UUID.randomUUID().toString(),
            "Filen_min.pdf",
            dokumenterer
            );
    }

    private static VedleggDto.Dokumenterer uttak() {
        return new VedleggDto.Dokumenterer(
            VedleggDto.Dokumenterer.Type.UTTAK,
            null,
            List.of(new ÅpenPeriodeDto(LocalDate.now().minusMonths(1), LocalDate.now()))
        );
    }

    private static VedleggDto.Dokumenterer tilrettelegging() {
        return new VedleggDto.Dokumenterer(
            VedleggDto.Dokumenterer.Type.TILRETTELEGGING,
            new ArbeidsforholdDto(ArbeidsforholdDto.Type.VIRKSOMHET, Orgnummer.MAGIC, null, null),
            null
        );
    }

    private static VedleggDto.Dokumenterer annet() {
        return null;
    }
}
