package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;

@Import(MellomlagringController.class)
@WebMvcTest(controllers = MellomlagringController.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class MellomlagringControllerInputValideringTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private KryptertMellomlagring kryptertMellomlagring;

    @Test
    void gyldigInputFraSøknadGårIgjennomValideringen() throws Exception {
        var soknadRawString = """
            {"søknad":{"type":"foreldrepenger","harGodkjentVilkår":true,"søkersituasjon":{"situasjon":"fødsel","rolle":"mor"},"barn":{"type":"født","fødselsdatoer":["2021-12-05T00:00:00.000Z"],"antallBarn":1,"termindato":"2021-12-01T00:00:00.000Z","fnr":"3333333333"},"annenForelder":{"etternavn":"KNOTT","fornavn":"LITEN","fnr":"3333333333","utenlandskFnr":false,"kanIkkeOppgis":false,"harRettPåForeldrepenger":true,"erInformertOmSøknaden":true},"søker":{"erAleneOmOmsorg":false,"språkkode":"nb","harHattAnnenInntektSiste10Mnd":true,"harJobbetSomFrilansSiste10Mnd":false,"harJobbetSomSelvstendigNæringsdrivendeSiste10Mnd":false,"andreInntekterSiste10Mnd":[{"pågående":true,"tidsperiode":{"fom":"2022-03-21","tom":""},"type":"VENTELØNN_VARTPENGER","vedlegg":[{"id":"V4702098355741871205675616043987158","file":{},"filename":"BlackMarble_2016_464m_caribbean_labeled.png","filesize":13834144,"uploaded":true,"pending":false,"type":"anneninntektDokumentasjon","skjemanummer":"I000060","url":"https://foreldrepengesoknad-api.dev.nav.no/rest/storage/vedlegg/80a00fe4-64a2-4892-a4e0-e4da827b6a6c","uuid":"80a00fe4-64a2-4892-a4e0-e4da827b6a6c"}]}],"selvstendigNæringsdrivendeInformasjon":[]},"informasjonOmUtenlandsopphold":{"tidligereOpphold":[],"senereOpphold":[],"iNorgeSiste12Mnd":true,"iNorgeNeste12Mnd":true},"erEndringssøknad":false,"uttaksplan":[{"id":"86683047-29953-22230-19513-25744813217365","type":"uttak","forelder":"mor","konto":"FORELDREPENGER_FØR_FØDSEL","tidsperiode":{"fom":"2021-11-15T00:00:00.000Z","tom":"2021-12-03T00:00:00.000Z"}},{"id":"031840322-4281-2418-29523-4351220177934","type":"uttak","forelder":"mor","konto":"MØDREKVOTE","tidsperiode":{"fom":"2021-12-06T00:00:00.000Z","tom":"2022-03-18T00:00:00.000Z"},"ønskerSamtidigUttak":false,"gradert":false},{"id":"303461970-02376-03324-18032-0340294779341","type":"overføring","forelder":"mor","konto":"FEDREKVOTE","tidsperiode":{"fom":"2022-03-21T00:00:00.000Z","tom":"2022-06-10T00:00:00.000Z"},"årsak":"SYKDOM_ANNEN_FORELDER","vedlegg":[{"id":"V801990822353310828169152810208125190","file":{},"filename":"image (1).png","filesize":52200,"uploaded":true,"pending":false,"type":"dokumentasjonOverføringAvKvote","skjemanummer":"I000023","url":"https://foreldrepengesoknad-api.dev.nav.no/rest/storage/vedlegg/0be55cd5-da9e-4bfe-8c20-63db87e1d046","uuid":"0be55cd5-da9e-4bfe-8c20-63db87e1d046"}]}],"harGodkjentOppsummering":false,"vedlegg":[],
            "tilleggsopplysninger":{"begrunnelseForSenEndring":{"tekst":"What\n\nWhat","ekstraInformasjon":"SYKDOM_OG_UTTAK"}},"dekningsgrad":"100"},"version":3,"currentRoute":"/soknad/oppsummering","uttaksplanInfo":{"permisjonStartdato":"2021-11-15","skalIkkeHaUttakFørTermin":false,"fellesperiodeukerMor":12},"antallUkerIUttaksplan":49}
            """;
        var result = mvc.perform(post(MellomlagringController.REST_STORAGE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(soknadRawString))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
        assertThat(result.getResolvedException()).isNull();
    }


    @Test
    void valideringBlokkererUgyldigeStrenger() {
        var soknadRawString = """
            {"søknad": "Inkludere et ulovlig teg her ¡®"}
            """;
        var exception = assertThrows(NestedServletException.class, () ->
            mvc.perform(post(MellomlagringController.REST_STORAGE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(soknadRawString)));
        assertThat(exception.getCause()).isInstanceOf(ConstraintViolationException.class);

    }

    @Test
    void hentVedleggOkMedUUID() throws Exception {
        var key = UUID.randomUUID();
        var result = mvc.perform(get(MellomlagringController.REST_STORAGE + "/vedlegg/{key}", key)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();
        assertThat(result.getResolvedException()).isNull();
    }

    @Test
    void hentVedleggHiverExceptionMedUlovligKey() {
        var key = "<Special=key∁\uD835\uDD4A>";
        var exception = assertThrows(NestedServletException.class, () ->
            mvc.perform(get(MellomlagringController.REST_STORAGE + "/vedlegg/{key}", key)
                .contentType(MediaType.APPLICATION_JSON)));
        assertThat(exception.getCause()).isInstanceOf(ConstraintViolationException.class);
    }

}
