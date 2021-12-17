package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Svangerskapspengesøknad;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JacksonConfiguration.class, FrontendSøknadTilSøknaDtoSeralizationDeseraliseringTest.InnsendingConnectionConfiguration.class})
class FrontendSøknadTilSøknaDtoSeralizationDeseraliseringTest {
    private static final Logger LOG = LoggerFactory.getLogger(FrontendSøknadTilSøknaDtoSeralizationDeseraliseringTest.class);

    @TestConfiguration
    static class InnsendingConnectionConfiguration {
        @Bean
        InnsendingConnection mockInnsendingConnection() {
            return new InnsendingConnection(null, null, new Image2PDFConverter());
        }
    }

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InnsendingConnection connection;

    @Test
    void søknadMappesTilIkkeTomtObjektTest() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_gradering.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);
        var body = connection.body(sf);
        assertThat(body).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"json/foreldrepenger_mor_gradering.json",
        "json/foreldrepenger_mor_utsettelse_og_utenlandsopphold.json",
        "json/foreldrepenger_far_gardering.json",
        "json/foreldrepenger_adopsjon_annenInntekt.json",
        "json/endringssøknad_termin_mor.json",
        "json/svangerskapspengesøknad.json"})
    void mapperTilMottakDtoSeraliseringDeseraliseringTest(String file) throws IOException {
        var sf = mapper.readValue(bytesFra(file), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);
        testDeseraliseringProdusererSammeObjekt(sf);
    }

    @Test
    void svangerskapspenger_mapping_til_dto_test() throws IOException {
        var sf = mapper.readValue(bytesFra("json/svangerskapspengesøknad.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);

        // Verifiser korrekt seralisering fra frontend
        assertThat(sf).isInstanceOf(Svangerskapspengesøknad.class);
        var svpSøknad = (Svangerskapspengesøknad) sf;
        assertThat(svpSøknad.getTilrettelegging()).hasSize(1);
        var tilrettelegging = svpSøknad.getTilrettelegging().get(0);
        assertThat(tilrettelegging.getType()).isEqualTo("delvis");
        assertThat(tilrettelegging.getStillingsprosent()).isEqualTo(50);
        assertThat(tilrettelegging.getArbeidsforhold()).isNotNull();
        assertThat(tilrettelegging.getBehovForTilretteleggingFom()).isNotNull();
        assertThat(tilrettelegging.getTilrettelagtArbeidFom()).isNotNull();
        assertThat(svpSøknad.getVedlegg()).isNotNull();
        assertThat(svpSøknad.getVedlegg().get(0).getId()).isEqualTo(tilrettelegging.getVedlegg().get(0));

        // Verifiser korrekt seralisering og deseralisering ned mot mottak
        testDeseraliseringProdusererSammeObjekt(sf);
    }

    @Test
    void engangsstønad_mapping_til_dto_test() throws IOException {
        var sf = mapper.readValue(bytesFra("json/engangsstønad.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);
        assertThat(sf).isInstanceOf(Engangsstønad.class);
        var engangsstønad = (Engangsstønad) sf;
        assertThat(engangsstønad.getType()).isEqualTo("engangsstønad");
        var barn = engangsstønad.getBarn();
        assertThat(barn).isNotNull();
        assertThat(barn.antallBarn).isEqualTo(2);
        assertThat(barn.erBarnetFødt).isFalse();
        assertThat(barn.terminbekreftelse).isNotNull();
        assertThat(barn.termindato).isNotNull();

        var informasjonOmUtenlandsopphold = engangsstønad.getInformasjonOmUtenlandsopphold();
        assertThat(informasjonOmUtenlandsopphold).isNotNull();
        assertThat(informasjonOmUtenlandsopphold.getSenereOpphold()).isEmpty();
        assertThat(informasjonOmUtenlandsopphold.getTidligereOpphold()).isEmpty();

        testDeseraliseringProdusererSammeObjekt(sf);
    }


    private void testDeseraliseringProdusererSammeObjekt(no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad søknadFraFrontend) {
        testDeseraliseringProdusererSammeObjekt(søknadFraFrontend, false);
    }

    private void testDeseraliseringProdusererSammeObjekt(no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad søknadFraFrontend, boolean log) {
        var søknad = connection.body(søknadFraFrontend);
        try {
            if (log) {
                LOG.info("{}", søknad);
            }
            var serialized = serialize(søknad);
            if (log) {
                LOG.info("Serialized as {}", serialized);
            }
            var deserialized = mapper.readValue(serialized, søknad.getClass());
            if (log) {
                LOG.info("{}", deserialized);
            }
            assertEquals(søknad, deserialized);
        } catch (Exception e) {
            LOG.error("Oops", e);
            fail(søknad.getClass().getSimpleName() + " failed");
        }
    }

    private String serialize(Object obj) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
