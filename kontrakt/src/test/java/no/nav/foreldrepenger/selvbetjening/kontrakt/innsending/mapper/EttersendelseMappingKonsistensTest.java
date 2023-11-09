package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.common.domain.felles.InnsendingsType.LASTET_OPP;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.EttersendingsType;
import no.nav.foreldrepenger.common.domain.felles.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.YtelseType;

class EttersendelseMappingKonsistensTest {

    @Test
    void testEttersendingMapper() {
        var saksnummer = new Saksnummer("952003131");
        var vedleggerDto = List.of(
            new VedleggDto(
                new byte[] {52, 12, 12, 32, 32, 32, 31, 2},
                "beskrivelse",
                new VedleggDto.Referanse("123456789"),
                LASTET_OPP.name(),
                "I000044",
                UUID.randomUUID().toString(),
                URI.create("https://foreldrepengesoknad-api.intern.dev.nav.no/rest/storage/vedlegg/b9974360-6c07-4b9d-acac-14f0f417d200")
            )
        );

        var ettersendelseFraFrontend = new EttersendelseDto(null, YtelseType.FORELDREPENGER, saksnummer, null, null, vedleggerDto);
        assertThat(ettersendelseFraFrontend.erTilbakebetalingUttalelse()).isFalse();
        var ettersendelse = EttersendingMapper.tilEttersending(ettersendelseFraFrontend);
        assertThat(ettersendelse.type()).isEqualTo(EttersendingsType.FORELDREPENGER);
        assertThat(ettersendelse.saksnr()).isEqualTo(saksnummer);
        assertThat(ettersendelse.vedlegg())
            .hasSameSizeAs(vedleggerDto)
            .extracting(v -> v.getMetadata().innsendingsType())
            .containsExactly(LASTET_OPP);
        assertThat(ettersendelse.vedlegg())
            .extracting(Vedlegg::getDokumentType)
            .containsExactly(DokumentType.I000044);
        var metadataVedlegg = ettersendelse.vedlegg().get(0).getMetadata();
        assertThat(metadataVedlegg.id().referanse()).isEqualTo(vedleggerDto.get(0).getId().referanse());
        assertThat(metadataVedlegg.dokumentType()).isEqualTo(DokumentType.I000044);
    }
}
