package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class VedleggsHåndteringTjenesteTest {

    private final byte[] CONVERTET_TO_BYTE = {25, 50, 44, 46};
    @Autowired
    private ObjectMapper mapper;

    private final Image2PDFConverter converter = mock(Image2PDFConverter.class);

    @Test
    void søknadSkalVæreLikNårDetIkkeFinnesDupliserteVedlegg() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), SøknadDto.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), SøknadDto.class); // For å ikke lage kopi

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedleggFraSøknad(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.vedlegg()).hasSameSizeAs(søknadDupliserteVedleggFjernet.vedlegg());
        assertThat(søknadOrginal).isEqualTo(søknadDupliserteVedleggFjernet);
    }

    @Test
    void fjernerDupliserteVedleggFraSøknad() throws IOException {
        when(converter.convert(any())).thenReturn(CONVERTET_TO_BYTE);
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt_dupliserte_vedlegg.json"), SøknadDto.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt_dupliserte_vedlegg.json"), SøknadDto.class); // For å ikke lage kopi

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedleggFraSøknad(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.vedlegg()).hasSize(4);
        assertThat(søknadOrginal.barn().omsorgsovertakelse().stream()
                .distinct()
                .count()).isEqualTo(3);
        assertThat(søknadDupliserteVedleggFjernet.vedlegg()).hasSize(2);
        assertThat(søknadDupliserteVedleggFjernet.barn().omsorgsovertakelse().stream()
                .distinct()
                .count()).isEqualTo(1);
        assertThat(søknadDupliserteVedleggFjernet.vedlegg())
            .extracting(VedleggDto::getContent)
            .containsOnly(CONVERTET_TO_BYTE);
    }

    @Test
    void fjernerDupliserteVedleggFraSøknadVedGradering() throws IOException {
        when(converter.convert(any())).thenReturn(CONVERTET_TO_BYTE);
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_duplisert_vedlegg.json"), SøknadDto.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_duplisert_vedlegg.json"), SøknadDto.class); // For å ikke lage kopi

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedleggFraSøknad(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.vedlegg()).hasSize(2);
        assertThat(søknadDupliserteVedleggFjernet.vedlegg()).hasSize(1);
        assertThat(søknadDupliserteVedleggFjernet.vedlegg())
            .extracting(VedleggDto::getContent)
            .containsOnly(CONVERTET_TO_BYTE);
    }

    @Test
    void skalFjerneDuplikateVedleggFraAnnenInntektOgDokumentasjonAvAktivitetskrav() throws IOException {
        when(converter.convert(any())).thenReturn(CONVERTET_TO_BYTE);
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_far_fellesperiode_førstegangstjenste_duplikate_vedlegg.json"), SøknadDto.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_far_fellesperiode_førstegangstjenste_duplikate_vedlegg.json"), SøknadDto.class); // For å ikke lage kopi

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedleggFraSøknad(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.vedlegg()).hasSize(9);
        assertThat(søknadDupliserteVedleggFjernet.vedlegg()).hasSize(5);
        assertThat(søknadDupliserteVedleggFjernet.vedlegg())
            .extracting(VedleggDto::getContent)
            .containsOnly(CONVERTET_TO_BYTE, null); // null er SEND_SENERE
    }

    @Test
    void skalIkkeFjerneVedleggDerHvorTypeSkjemaNummerErLiktMensInnholdErForskjellig() throws IOException {
        when(converter.convert(any())).thenAnswer(i -> i.getArguments()[0]);
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_to_vedlegg_samme_type_ulikt_innhold.json"), SøknadDto.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_to_vedlegg_samme_type_ulikt_innhold.json"), SøknadDto.class); // For å ikke lage kopi

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedleggFraSøknad(søknadDupliserteVedleggFjernet);

        assertThat(søknadDupliserteVedleggFjernet.vedlegg()).hasSameSizeAs(søknadOrginal.vedlegg());
        assertThat(søknadDupliserteVedleggFjernet).isEqualTo(søknadOrginal);
    }


    @Test
    void ettersendingerForblirUendretVedIngenDupliserteVedlegg() throws IOException {
        var ettersendelseOrginal = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), EttersendelseDto.class);
        var ettersendelseDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), EttersendelseDto.class);

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedleggFraEttersending(ettersendelseDupliserteVedleggFjernet);

        assertThat(ettersendelseOrginal.vedlegg()).hasSameSizeAs(ettersendelseDupliserteVedleggFjernet.vedlegg());
        assertThat(ettersendelseOrginal).isEqualTo(ettersendelseDupliserteVedleggFjernet);
    }

    @Test
    void duplikateVedleggFjernesFraEttersending() throws IOException {
        when(converter.convert(any())).thenReturn(CONVERTET_TO_BYTE);
        var ettersendelseOrginal = mapper.readValue(bytesFra("json/ettersendelse_I000044_duplikate_vedlegg.json"), EttersendelseDto.class);
        var ettersendelseDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/ettersendelse_I000044_duplikate_vedlegg.json"), EttersendelseDto.class);

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedleggFraEttersending(ettersendelseDupliserteVedleggFjernet);

        assertThat(ettersendelseOrginal.vedlegg()).hasSize(3);
        assertThat(ettersendelseDupliserteVedleggFjernet.vedlegg()).hasSize(1);
        assertThat(ettersendelseDupliserteVedleggFjernet.vedlegg())
            .extracting(VedleggDto::getContent)
            .containsOnly(CONVERTET_TO_BYTE);
    }

}
