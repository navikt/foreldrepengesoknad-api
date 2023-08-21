package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class VedleggsHåndteringTjenesteTest {

    @Autowired
    private ObjectMapper mapper;

    private final Image2PDFConverter converter = mock(Image2PDFConverter.class);

    @BeforeEach
    public void setup() {
        when(converter.convert(any())).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    void søknadSkalVæreLikNårDetIkkeFinnesDupliserteVedlegg() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), SøknadFrontend.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), SøknadFrontend.class); // For å ikke lage kopi

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedlegg(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.getVedlegg()).hasSameSizeAs(søknadDupliserteVedleggFjernet.getVedlegg());
        assertThat(søknadOrginal).isEqualTo(søknadDupliserteVedleggFjernet);
    }

    @Test
    void fjernerDupliserteVedleggFraSøknad() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt_dupliserte_vedlegg.json"), SøknadFrontend.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt_dupliserte_vedlegg.json"), SøknadFrontend.class); // For å ikke lage kopi

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedlegg(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.getVedlegg()).hasSize(4);
        assertThat(søknadOrginal.getBarn().omsorgsovertakelse().stream()
                .distinct()
                .count()).isEqualTo(3);
        assertThat(søknadDupliserteVedleggFjernet.getVedlegg()).hasSize(2);
        assertThat(søknadDupliserteVedleggFjernet.getBarn().omsorgsovertakelse().stream()
                .distinct()
                .count()).isEqualTo(1);
    }

    @Test
    void fjernerDupliserteVedleggFraSøknadVedGradering() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_duplisert_vedlegg.json"), SøknadFrontend.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_duplisert_vedlegg.json"), SøknadFrontend.class); // For å ikke lage kopi

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedlegg(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.getVedlegg()).hasSize(2);
        assertThat(søknadDupliserteVedleggFjernet.getVedlegg()).hasSize(1);
        søknadDupliserteVedleggFjernet.setVedlegg(søknadOrginal.getVedlegg());
        assertThat(søknadDupliserteVedleggFjernet).isEqualTo(søknadOrginal);
    }

    @Test
    void skalFjerneDuplikateVedleggFraAnnenInntektOgDokumentasjonAvAktivitetskrav() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_far_fellesperiode_førstegangstjenste_duplikate_vedlegg.json"), SøknadFrontend.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_far_fellesperiode_førstegangstjenste_duplikate_vedlegg.json"), SøknadFrontend.class); // For å ikke lage kopi

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedlegg(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.getVedlegg()).hasSize(9);
        assertThat(søknadDupliserteVedleggFjernet.getVedlegg()).hasSize(5);
    }

    @Test
    void skalIkkeFjerneVedleggDerHvorTypeSkjemaNummerErLiktMensInnholdErForskjellig() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_to_vedlegg_samme_type_ulikt_innhold.json"), SøknadFrontend.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_to_vedlegg_samme_type_ulikt_innhold.json"), SøknadFrontend.class); // For å ikke lage kopi

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedlegg(søknadDupliserteVedleggFjernet);

        assertThat(søknadDupliserteVedleggFjernet.getVedlegg()).hasSameSizeAs(søknadOrginal.getVedlegg());
        assertThat(søknadDupliserteVedleggFjernet).isEqualTo(søknadOrginal);
    }


    @Test
    void ettersendingerForblirUendretVedIngenDupliserteVedlegg() throws IOException {
        var ettersendelseOrginal = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), SøknadFrontend.class);
        var ettersendelseDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), SøknadFrontend.class);

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedlegg(ettersendelseDupliserteVedleggFjernet);

        assertThat(ettersendelseOrginal.getVedlegg()).hasSameSizeAs(ettersendelseDupliserteVedleggFjernet.getVedlegg());
        assertThat(ettersendelseOrginal).isEqualTo(ettersendelseDupliserteVedleggFjernet);
    }

    @Test
    void duplikateVedleggFjernesFraEttersending() throws IOException {
        var ettersendelseOrginal = mapper.readValue(bytesFra("json/ettersendelse_I000044_duplikate_vedlegg.json"), SøknadFrontend.class);
        var ettersendelseDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/ettersendelse_I000044_duplikate_vedlegg.json"), SøknadFrontend.class);

        var vedleggsHåndteringsTjeneste = new VedleggsHåndteringTjeneste(converter);
        vedleggsHåndteringsTjeneste.fjernDupliserteVedlegg(ettersendelseDupliserteVedleggFjernet);

        assertThat(ettersendelseOrginal.getVedlegg()).hasSize(3);
        assertThat(ettersendelseDupliserteVedleggFjernet.getVedlegg()).hasSize(1);

        ettersendelseDupliserteVedleggFjernet.setVedlegg(ettersendelseOrginal.getVedlegg());
        assertThat(ettersendelseOrginal).isEqualTo(ettersendelseDupliserteVedleggFjernet);
    }

}
