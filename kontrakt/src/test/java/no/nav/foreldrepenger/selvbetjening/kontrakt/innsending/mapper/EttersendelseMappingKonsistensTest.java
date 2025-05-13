package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.common.domain.felles.DokumentType.I000044;
import static no.nav.foreldrepenger.common.domain.felles.InnsendingsType.LASTET_OPP;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.domain.felles.EttersendingsType;
import no.nav.foreldrepenger.common.domain.felles.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.YtelseType;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.VedleggInnsendingType;

class EttersendelseMappingKonsistensTest {

    @Test
    void testEttersendingMapper() {
        var saksnummer = new Saksnummer("952003131");
        var vedlegg = new VedleggDto(UUID.randomUUID(), I000044, VedleggInnsendingType.LASTET_OPP, "beskrivelse", null, null);
        var vedleggerDto = List.of(vedlegg);

        var ettersendelseFraFrontend = new EttersendelseDto(null, YtelseType.FORELDREPENGER, saksnummer, null, null, vedleggerDto);
        assertThat(ettersendelseFraFrontend.erTilbakebetalingUttalelse()).isFalse();
        var ettersendelse = EttersendingMapper.tilEttersending(ettersendelseFraFrontend);
        assertThat(ettersendelse.type()).isEqualTo(EttersendingsType.FORELDREPENGER);
        assertThat(ettersendelse.saksnr()).isEqualTo(saksnummer);
        assertThat(ettersendelse.vedlegg()).hasSameSizeAs(vedleggerDto)
            .extracting(v -> v.getMetadata().innsendingsType())
            .containsExactly(LASTET_OPP);
        assertThat(ettersendelse.vedlegg()).extracting(Vedlegg::getDokumentType).containsExactly(I000044);
        var metadataVedlegg = ettersendelse.vedlegg().getFirst().getMetadata();
        assertThat(metadataVedlegg.id().referanse()).isEqualTo(vedleggerDto.getFirst().referanse().verdi()).startsWith("V");
        assertThat(metadataVedlegg.dokumentType()).isEqualTo(I000044);
    }
}
