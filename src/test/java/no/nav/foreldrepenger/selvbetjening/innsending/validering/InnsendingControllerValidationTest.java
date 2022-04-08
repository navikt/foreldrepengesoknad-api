package no.nav.foreldrepenger.selvbetjening.innsending.validering;


import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.error.ApiExceptionHandler;
import no.nav.foreldrepenger.selvbetjening.innsending.Innsending;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingController;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.ForeldrepengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SvangerskapspengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.util.TokenUtil;

@Import({InnsendingController.class, ApiExceptionHandler.class})
@WebMvcTest(controllers = InnsendingController.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class InnsendingControllerValidationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private Innsending innsending;

    @MockBean
    private TokenUtil tokenUtil;

    @Test
    void foreldrepengesoknadFrilansOgVedleggValidering() throws Exception {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans.json"), SøknadFrontend.class);
        var fpSøknad = (ForeldrepengesøknadFrontend) sf;
        var result = mvc.perform(post(InnsendingController.INNSENDING_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sf)
                    .replace(fpSøknad.getAnnenForelder().fornavn(), "Ulovlig tegn [≈≈|£©≈[™")
                    .replace(fpSøknad.getSøker().frilansInformasjon().oppdragForNæreVennerEllerFamilieSiste10Mnd().get(0).navnPåArbeidsgiver(), "Matematiske tegn er ikke lov ∪")
                    .replaceFirst(fpSøknad.getVedlegg().get(0).getSkjemanummer(), "<scri>sp 202≈≈|0")
                ))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class);
        var error = (MethodArgumentNotValidException) result.getResolvedException();
        assertThat(error).isNotNull();
        assertThat(error.getBindingResult().getFieldErrors()).hasSize(3);
    }


    @Test
    void foreldrepengesoknadAnnenInntektValidering() throws Exception {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), SøknadFrontend.class);
        var fpSøknad = (ForeldrepengesøknadFrontend) sf;
        var annenInntektFrontend = fpSøknad.getSøker().andreInntekterSiste10Mnd().get(0);
        var result = mvc.perform(post(InnsendingController.INNSENDING_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sf)
                    .replace(annenInntektFrontend.arbeidsgiverNavn(), "Ulovlig tegn [≈≈|£©≈[™")
                    .replaceFirst(annenInntektFrontend.vedlegg().get(0), "Also ILLEGA @¨¨¨¨ö~~π<>")
                ))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class);
        var error = (MethodArgumentNotValidException) result.getResolvedException();
        assertThat(error).isNotNull();
        assertThat(error.getBindingResult().getFieldErrors()).hasSize(2);
    }


    @Test
    void foreldrepengesoknadEgennæringValidering() throws Exception {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_gradering_egenNæring_og_frilans.json"), SøknadFrontend.class);
        var fpSøknad = (ForeldrepengesøknadFrontend) sf;
        var egennæring = fpSøknad.getSøker().selvstendigNæringsdrivendeInformasjon().get(0);
        var result = mvc.perform(post(InnsendingController.INNSENDING_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sf)
                    .replace(egennæring.navnPåNæringen(), "Ulovlig tegn [≈≈|£©≈[™")
                    .replace(egennæring.regnskapsfører().navn(), "Also ILLEGA @¨¨¨¨ö~~π<>")
                ))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class);
        var error = (MethodArgumentNotValidException) result.getResolvedException();
        assertThat(error).isNotNull();
        assertThat(error.getBindingResult().getFieldErrors()).hasSize(2);
    }


    @Test
    void svangerskapspengerValidering() throws Exception {
        var sf = mapper.readValue(bytesFra("json/svangerskapspengesøknad.json"), SøknadFrontend.class);
        var svpSøknad = (SvangerskapspengesøknadFrontend) sf;
        var tilrettelegging = svpSøknad.getTilrettelegging().get(0);
        var result = mvc.perform(post(InnsendingController.INNSENDING_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sf)
                    .replace(tilrettelegging.type(), "Ulovlig tegn [≈≈|£©≈[™")
                    .replaceFirst(tilrettelegging.vedlegg().get(0), "Also ILLEGA @¨¨¨¨ö~~π<>")
                    .replace(tilrettelegging.arbeidsforhold().id(), "Ikke lovlig \u0085")
                ))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class);
        var error = (MethodArgumentNotValidException) result.getResolvedException();
        assertThat(error).isNotNull();
        assertThat(error.getBindingResult().getFieldErrors()).hasSize(3);
    }


    @Test
    void endringssøknadSaksnummerValidering() throws Exception {
        var sf = mapper.readValue(bytesFra("json/endringssøknad_termin_mor.json"), SøknadFrontend.class);
        var fpSøknad = (ForeldrepengesøknadFrontend) sf;
        var result = mvc.perform(post(InnsendingController.INNSENDING_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sf)
                    .replace(fpSøknad.getSaksnummer(), "Ulovlig tegn [≈≈|£©≈[™")
                    .replace(fpSøknad.getSituasjon(), "Lattidattidudadaääö˙[`")
                ))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class);
        var error = (MethodArgumentNotValidException) result.getResolvedException();
        assertThat(error).isNotNull();
        assertThat(error.getBindingResult().getFieldErrors()).hasSize(2);
    }

}
