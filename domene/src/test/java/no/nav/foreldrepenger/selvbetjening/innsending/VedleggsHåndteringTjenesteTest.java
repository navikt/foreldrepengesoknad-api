package no.nav.foreldrepenger.selvbetjening.innsending;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class VedleggsHåndteringTjenesteTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    void søknadSkalVæreLikNårDetIkkeFinnesDupliserteVedlegg() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), SøknadDto.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), SøknadDto.class); // For å ikke lage kopi

        VedleggsHåndteringTjeneste.fjernDupliserteVedleggFraInnsending(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.vedlegg()).hasSameSizeAs(søknadDupliserteVedleggFjernet.vedlegg());
        assertThat(søknadOrginal).isEqualTo(søknadDupliserteVedleggFjernet);
    }

    @Test
    void fjernerDupliserteVedleggFraSøknad() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt_dupliserte_vedlegg.json"), SøknadDto.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt_dupliserte_vedlegg.json"), SøknadDto.class); // For å ikke lage kopi

        VedleggsHåndteringTjeneste.fjernDupliserteVedleggFraInnsending(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.vedlegg()).hasSize(4);
        assertThat(søknadOrginal.barn().omsorgsovertakelse().stream()
                .distinct()
                .count()).isEqualTo(3);
        assertThat(søknadDupliserteVedleggFjernet.vedlegg()).hasSize(2);
        assertThat(søknadDupliserteVedleggFjernet.barn().omsorgsovertakelse().stream()
                .distinct()
                .count()).isEqualTo(1);
    }

    @Test
    void fjernerDupliserteVedleggFraSøknadVedGradering() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_duplisert_vedlegg.json"), SøknadDto.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_duplisert_vedlegg.json"), SøknadDto.class); // For å ikke lage kopi

        VedleggsHåndteringTjeneste.fjernDupliserteVedleggFraInnsending(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.vedlegg()).hasSize(2);
        assertThat(søknadDupliserteVedleggFjernet.vedlegg()).hasSize(1);
    }

    @Test
    void skalFjerneDuplikateVedleggFraAnnenInntektOgDokumentasjonAvAktivitetskrav() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_far_fellesperiode_førstegangstjenste_duplikate_vedlegg.json"), SøknadDto.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_far_fellesperiode_førstegangstjenste_duplikate_vedlegg.json"), SøknadDto.class); // For å ikke lage kopi

        VedleggsHåndteringTjeneste.fjernDupliserteVedleggFraInnsending(søknadDupliserteVedleggFjernet);

        assertThat(søknadOrginal.vedlegg()).hasSize(9);
        assertThat(søknadDupliserteVedleggFjernet.vedlegg()).hasSize(5);
    }

    @Test
    void skalIkkeFjerneVedleggDerHvorTypeSkjemaNummerErLiktMensInnholdErForskjellig() throws IOException {
        var søknadOrginal = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_to_vedlegg_samme_type_ulikt_innhold.json"), SøknadDto.class);
        var søknadDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans_to_vedlegg_samme_type_ulikt_innhold.json"), SøknadDto.class); // For å ikke lage kopi

        VedleggsHåndteringTjeneste.fjernDupliserteVedleggFraInnsending(søknadDupliserteVedleggFjernet);

        assertThat(søknadDupliserteVedleggFjernet.vedlegg()).hasSameSizeAs(søknadOrginal.vedlegg());
        assertThat(søknadDupliserteVedleggFjernet).isEqualTo(søknadOrginal);
    }


    @Test
    void ettersendingerForblirUendretVedIngenDupliserteVedlegg() throws IOException {
        var ettersendelseOrginal = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), EttersendelseDto.class);
        var ettersendelseDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), EttersendelseDto.class);

        VedleggsHåndteringTjeneste.fjernDupliserteVedleggFraInnsending(ettersendelseDupliserteVedleggFjernet);

        assertThat(ettersendelseOrginal.vedlegg()).hasSameSizeAs(ettersendelseDupliserteVedleggFjernet.vedlegg());
        assertThat(ettersendelseOrginal).isEqualTo(ettersendelseDupliserteVedleggFjernet);
    }

    @Test
    void duplikateVedleggFjernesFraEttersending() throws IOException {
        var ettersendelseOrginal = mapper.readValue(bytesFra("json/ettersendelse_I000044_duplikate_vedlegg.json"), EttersendelseDto.class);
        var ettersendelseDupliserteVedleggFjernet = mapper.readValue(bytesFra("json/ettersendelse_I000044_duplikate_vedlegg.json"), EttersendelseDto.class);

        VedleggsHåndteringTjeneste.fjernDupliserteVedleggFraInnsending(ettersendelseDupliserteVedleggFjernet);

        assertThat(ettersendelseOrginal.vedlegg()).hasSize(3);
        assertThat(ettersendelseDupliserteVedleggFjernet.vedlegg()).hasSize(1);
    }

}
