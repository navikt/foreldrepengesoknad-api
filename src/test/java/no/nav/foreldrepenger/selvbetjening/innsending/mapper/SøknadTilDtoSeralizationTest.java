package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Svangerskapspengesøknad;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JacksonConfiguration.class, SøknadTilDtoSeralizationTest.InnsendingConnectionConfiguration.class})
class SøknadTilDtoSeralizationTest {
    private static final Logger LOG = LoggerFactory.getLogger(SøknadTilDtoSeralizationTest.class);

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
    void testBody() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_gradering.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);
        var body = connection.body(sf);
        assertThat(body).isNotNull();
    }

    @Test
    void InnsendtSøknadFraFrontendDeseraliseresKorrektTest() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_gradering.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);

        // Verifiser korrekt seralisering fra frontend
        assertThat(sf).isInstanceOf(Foreldrepengesøknad.class);
        var fs = (Foreldrepengesøknad) sf;
        assertThat(fs.getType()).isEqualTo("foreldrepenger");
        assertThat(fs.getSituasjon()).isEqualTo("fødsel");
        assertThat(fs.getErEndringssøknad()).isFalse();
        assertThat(fs.getDekningsgrad()).isEqualTo("80");

        assertThat(fs.getUttaksplan()).hasSize(3);
        var uttaksperiode1 = fs.getUttaksplan().get(0);
        assertThat(uttaksperiode1.getForelder()).isEqualTo("mor");
        assertThat(uttaksperiode1.getType()).isEqualTo("uttak");
        assertThat(uttaksperiode1.getKonto()).isEqualTo("FORELDREPENGER_FØR_FØDSEL");
        assertThat(uttaksperiode1.getGradert()).isNull();
        assertThat(uttaksperiode1.getØnskerSamtidigUttak()).isFalse();
        assertThat(uttaksperiode1.getTidsperiode()).isNotNull();
        assertThat(uttaksperiode1.getTidsperiode().getFom()).isNotNull();
        assertThat(uttaksperiode1.getTidsperiode().getTom()).isNotNull();
        assertThat(uttaksperiode1.getForelder()).isEqualTo("mor");

        var uttaksperiode2 = fs.getUttaksplan().get(1);
        assertThat(uttaksperiode2.getForelder()).isEqualTo("mor");
        assertThat(uttaksperiode2.getType()).isEqualTo("uttak");
        assertThat(uttaksperiode2.getKonto()).isEqualTo("FORELDREPENGER");
        assertThat(uttaksperiode2.getGradert()).isFalse();
        assertThat(uttaksperiode2.getTidsperiode()).isNotNull();
        assertThat(uttaksperiode2.getTidsperiode().getFom()).isNotNull();
        assertThat(uttaksperiode2.getTidsperiode().getTom()).isNotNull();

        var uttaksperiode3 = fs.getUttaksplan().get(2);
        assertThat(uttaksperiode3.getForelder()).isEqualTo("mor");
        assertThat(uttaksperiode3.getType()).isEqualTo("uttak");
        assertThat(uttaksperiode3.getKonto()).isEqualTo("FORELDREPENGER");
        assertThat(uttaksperiode3.getTidsperiode()).isNotNull();
        assertThat(uttaksperiode3.getTidsperiode().getFom()).isNotNull();
        assertThat(uttaksperiode3.getTidsperiode().getTom()).isNotNull();
        assertThat(uttaksperiode3.getErArbeidstaker()).isTrue();
        assertThat(uttaksperiode3.getErFrilanser()).isFalse();
        assertThat(uttaksperiode3.getØnskerSamtidigUttak()).isFalse();
        assertThat(uttaksperiode3.getGradert()).isTrue();
        assertThat(uttaksperiode3.getOrgnumre()).hasSize(1);
        assertThat(uttaksperiode3.getStillingsprosent()).isEqualTo(45.0);


        var søker = fs.getSøker();
        assertThat(søker).isNotNull();
        assertThat(søker.getRolle()).isEqualTo("MOR");
        assertThat(søker.getSpråkkode()).isEqualTo("NB");
        assertThat(søker.getErAleneOmOmsorg()).isTrue();
        var frilansInformasjon = søker.getFrilansInformasjon();
        assertThat(frilansInformasjon).isNotNull();
        assertThat(frilansInformasjon.getOppstart()).isNotNull();
        assertThat(frilansInformasjon.getDriverFosterhjem()).isFalse();
        assertThat(frilansInformasjon.getOppdragForNæreVennerEllerFamilieSiste10Mnd()).hasSize(1);
        var frilansoppdrag = frilansInformasjon.getOppdragForNæreVennerEllerFamilieSiste10Mnd().get(0);
        assertThat(frilansoppdrag.getNavnPåArbeidsgiver()).isEqualTo("Klara Klukk");
        assertThat(frilansoppdrag.getTidsperiode()).isNotNull();
        assertThat(frilansoppdrag.getTidsperiode().getFom()).isNotNull();
        assertThat(frilansoppdrag.getTidsperiode().getTom()).isNotNull();
        var selvstendigNæringsdrivendeInfo = søker.getSelvstendigNæringsdrivendeInformasjon();
        assertThat(selvstendigNæringsdrivendeInfo).hasSize(1);
        var selvstendigNæringsdrivendeInformasjon = selvstendigNæringsdrivendeInfo.get(0);
        assertThat(selvstendigNæringsdrivendeInformasjon.getNæringstyper()).hasSize(1);
        assertThat(selvstendigNæringsdrivendeInformasjon.getTidsperiode()).isNotNull();
        assertThat(selvstendigNæringsdrivendeInformasjon.getTidsperiode().getFom()).isNotNull();
        assertThat(selvstendigNæringsdrivendeInformasjon.getTidsperiode().getTom()).isNull();
        assertThat(selvstendigNæringsdrivendeInformasjon.getOrganisasjonsnummer()).isEqualTo("999999999");
        assertThat(selvstendigNæringsdrivendeInformasjon.getHarBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene()).isFalse();
        assertThat(selvstendigNæringsdrivendeInformasjon.getRegnskapsfører()).isNotNull();
        assertThat(selvstendigNæringsdrivendeInformasjon.getNæringsinntekt()).isEqualTo(220_000);
        assertThat(selvstendigNæringsdrivendeInformasjon.getNæringstyper()).hasSize(1).contains("DAGMAMMA");

        var barn = sf.getBarn();
        assertThat(barn).isNotNull();
        assertThat(barn.erBarnetFødt).isTrue();
        assertThat(barn.fødselsdatoer).hasSize(1);
        assertThat(barn.antallBarn).isEqualTo(1);
        assertThat(barn.termindato).isNotNull();
        assertThat(barn.terminbekreftelse).isEmpty();

        var annenForelder = sf.getAnnenForelder();
        assertThat(annenForelder).isNotNull();
        assertThat(annenForelder.getKanIkkeOppgis()).isFalse();
        assertThat(annenForelder.getFornavn()).isNotNull();
        assertThat(annenForelder.getEtternavn()).isNotNull();
        assertThat(annenForelder.getFnr()).isEqualTo("11111122222");

        var informasjonOmUtenlandsopphold = sf.getInformasjonOmUtenlandsopphold();
        assertThat(informasjonOmUtenlandsopphold).isNotNull();
        assertThat(informasjonOmUtenlandsopphold.getTidligereOpphold()).isEmpty();
        assertThat(informasjonOmUtenlandsopphold.getSenereOpphold()).hasSize(1);
        assertThat(informasjonOmUtenlandsopphold.getSenereOpphold().get(0).getLand()).isEqualTo("FI");
        assertThat(informasjonOmUtenlandsopphold.getSenereOpphold().get(0).getTidsperiode().getFom()).isNotNull();
        assertThat(informasjonOmUtenlandsopphold.getSenereOpphold().get(0).getTidsperiode().getTom()).isNotNull();

        assertThat(sf.getVedlegg()).isEmpty();
    }

    @Test
    void foreldrepenger_gradering_mor_med_mapping_til_dto_test() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_gradering.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);
        testDeseraliseringProdusererSammeObjekt(sf);
    }


    @Test
    void foreldrepenger_mor_utsettelse_og_utenlandsopphold_mapping_til_dto_test() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_utsettelse_og_utenlandsopphold.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);
        testDeseraliseringProdusererSammeObjekt(sf);
    }

    @Test
    void foreldrepenger_far_gardering_mapping_til_dto_test() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);
        testDeseraliseringProdusererSammeObjekt(sf);
    }

    @Test
    void foreldrepenger_adopsjon_annenInntekt_mapping_til_dto_test() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);
        testDeseraliseringProdusererSammeObjekt(sf);
    }

    @Test
    void endringssøknad_termin_mor_mapping_til_dto_test() throws IOException {
        var sf = mapper.readValue(bytesFra("json/endringssøknad_termin_mor.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad.class);
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
        try {
            var søknadOLDDto = connection.body(søknadFraFrontend);
            var seralized_old = serialize(søknadOLDDto);
            var deseralized_old = mapper.readValue(seralized_old, Søknad.class);

            var søknadNY = connection.tilSøknadDtoNY(søknadFraFrontend);
            var seralized_ny = serialize(søknadNY);
            var deseralized_ny = mapper.readValue(seralized_ny, Søknad.class);
            assertThat(deseralized_old).isEqualTo(deseralized_ny);
        } catch (Exception e) {
            LOG.error("Oops", e);
            fail(søknadFraFrontend.getClass().getSimpleName() + " failed");
        }
    }

    private String serialize(Object obj) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }
}
