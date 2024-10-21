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
import no.nav.foreldrepenger.selvbetjening.http.TokenUtil;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingController;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingTjeneste;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.arbeidsforhold.VirksomhetDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.SvangerskapspengesøknadDto;

@Import({InnsendingController.class, ApiExceptionHandler.class})
@WebMvcTest(controllers = InnsendingController.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class InnsendingControllerValidationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private InnsendingTjeneste innsending;

    @MockBean
    private TokenUtil tokenUtil;

    @Test
    void foreldrepengesoknadFrilansOgVedleggValidering() throws Exception {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans.json"), SøknadDto.class);
        var fpSøknad = (ForeldrepengesøknadDto) sf;
        var result = mvc.perform(post(InnsendingController.INNSENDING_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sf)
                    .replace(fpSøknad.annenForelder().fornavn(), "Ulovlig tegn [≈≈|£©≈[™")
                ))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class);
        var error = (MethodArgumentNotValidException) result.getResolvedException();
        assertThat(error).isNotNull();
        assertThat(error.getBindingResult().getFieldErrors()).hasSize(1);
    }


    @Test
    void foreldrepengesoknadAnnenInntektValidering() throws Exception {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), SøknadDto.class);
        var fpSøknad = (ForeldrepengesøknadDto) sf;
        var annenInntektFrontend = fpSøknad.søker().andreInntekterSiste10Mnd().get(0);
        var result = mvc.perform(post(InnsendingController.INNSENDING_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sf)
                    .replace(annenInntektFrontend.arbeidsgiverNavn(), "Ulovlig tegn [≈≈|£©≈[™")
                ))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class);
        var error = (MethodArgumentNotValidException) result.getResolvedException();
        assertThat(error).isNotNull();
        assertThat(error.getBindingResult().getFieldErrors()).hasSize(1);
    }


    @Test
    void foreldrepengesoknadEgennæringValidering() throws Exception {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_gradering_egenNæring_og_frilans.json"), SøknadDto.class);
        var fpSøknad = (ForeldrepengesøknadDto) sf;
        var egennæring = fpSøknad.søker().selvstendigNæringsdrivendeInformasjon().get(0);
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
        var svpSøknad = mapper.readValue(bytesFra("json/svangerskapspengesøknad.json"), SvangerskapspengesøknadDto.class);
        var tilrettelegging = svpSøknad.tilrettelegging().get(0);
        var virksomhet = (VirksomhetDto) tilrettelegging.arbeidsforhold();
        var result = mvc.perform(post(InnsendingController.INNSENDING_CONTROLLER_PATH + "/svangerskapspenger")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(svpSøknad)
                    .replace(virksomhet.id().value(), "Ikke lovlig \u0085")
                ))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class);
        var error = (MethodArgumentNotValidException) result.getResolvedException();
        assertThat(error).isNotNull();
        assertThat(error.getBindingResult().getFieldErrors()).hasSize(4); // Feiler på både @Orgnmr og @Pattern begge plassene hvor dette er definert
    }


    @Test
    void endringssøknadSaksnummerValidering() throws Exception {
        var sf = mapper.readValue(bytesFra("json/endringssøknad_termin_mor.json"), EndringssøknadDto.class);
        var fpSøknad = (EndringssøknadForeldrepengerDto) sf;
        var result = mvc.perform(post(InnsendingController.INNSENDING_CONTROLLER_PATH + "/endre")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sf)
                    .replace(fpSøknad.saksnummer().value(), "Ulovlig tegn [≈≈|£©≈[™dudadaääö˙[`")
                ))
            .andExpect(status().isBadRequest())
            .andReturn();

        assertThat(result.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class);
        var error = (MethodArgumentNotValidException) result.getResolvedException();
        assertThat(error).isNotNull();
        assertThat(error.getBindingResult().getFieldErrors()).hasSize(1);
    }

}
