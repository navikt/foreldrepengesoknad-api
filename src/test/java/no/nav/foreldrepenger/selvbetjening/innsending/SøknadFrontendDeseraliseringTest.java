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
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Foreldrepengesøknad;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class SøknadFrontendDeseraliseringTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    void innsendtSøknadFraFrontendDeseraliseresKorrektTest() throws IOException {
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
        assertThat(uttaksperiode1.forelder()).isEqualTo("mor");
        assertThat(uttaksperiode1.type()).isEqualTo("uttak");
        assertThat(uttaksperiode1.konto()).isEqualTo("FORELDREPENGER_FØR_FØDSEL");
        assertThat(uttaksperiode1.gradert()).isFalse();
        assertThat(uttaksperiode1.ønskerSamtidigUttak()).isFalse();
        assertThat(uttaksperiode1.tidsperiode()).isNotNull();
        assertThat(uttaksperiode1.tidsperiode().fom()).isNotNull();
        assertThat(uttaksperiode1.tidsperiode().tom()).isNotNull()
            .isAfter(uttaksperiode1.tidsperiode().fom());
        assertThat(uttaksperiode1.forelder()).isEqualTo("mor");

        var uttaksperiode2 = fs.getUttaksplan().get(1);
        assertThat(uttaksperiode2.forelder()).isEqualTo("mor");
        assertThat(uttaksperiode2.type()).isEqualTo("uttak");
        assertThat(uttaksperiode2.konto()).isEqualTo("FORELDREPENGER");
        assertThat(uttaksperiode2.gradert()).isFalse();
        assertThat(uttaksperiode2.tidsperiode()).isNotNull();
        assertThat(uttaksperiode2.tidsperiode().fom()).isNotNull();
        assertThat(uttaksperiode2.tidsperiode().tom()).isNotNull()
            .isAfter(uttaksperiode2.tidsperiode().fom());

        var uttaksperiode3 = fs.getUttaksplan().get(2);
        assertThat(uttaksperiode3.forelder()).isEqualTo("mor");
        assertThat(uttaksperiode3.type()).isEqualTo("uttak");
        assertThat(uttaksperiode3.konto()).isEqualTo("FORELDREPENGER");
        assertThat(uttaksperiode3.tidsperiode()).isNotNull();
        assertThat(uttaksperiode3.tidsperiode().fom()).isNotNull();
        assertThat(uttaksperiode3.tidsperiode().tom()).isNotNull();
        assertThat(uttaksperiode3.tidsperiode().tom()).isNotNull()
            .isAfter(uttaksperiode3.tidsperiode().fom());
        assertThat(uttaksperiode3.erArbeidstaker()).isTrue();
        assertThat(uttaksperiode3.erFrilanser()).isFalse();
        assertThat(uttaksperiode3.ønskerSamtidigUttak()).isFalse();
        assertThat(uttaksperiode3.gradert()).isTrue();
        assertThat(uttaksperiode3.orgnumre()).hasSize(1);
        assertThat(uttaksperiode3.stillingsprosent()).isEqualTo(45.0);


        var søker = fs.getSøker();
        assertThat(søker).isNotNull();
        assertThat(søker.rolle()).isEqualTo("MOR");
        assertThat(søker.språkkode()).isEqualTo("NB");
        assertThat(søker.erAleneOmOmsorg()).isTrue();
        var frilansInformasjon = søker.frilansInformasjon();
        assertThat(frilansInformasjon).isNotNull();
        assertThat(frilansInformasjon.oppstart()).isNotNull();
        assertThat(frilansInformasjon.driverFosterhjem()).isFalse();
        assertThat(frilansInformasjon.oppdragForNæreVennerEllerFamilieSiste10Mnd()).hasSize(1);
        var frilansoppdrag = frilansInformasjon.oppdragForNæreVennerEllerFamilieSiste10Mnd().get(0);
        assertThat(frilansoppdrag.navnPåArbeidsgiver()).isEqualTo("Klara Klukk");
        assertThat(frilansoppdrag.tidsperiode()).isNotNull();
        assertThat(frilansoppdrag.tidsperiode().fom()).isNotNull();
        assertThat(frilansoppdrag.tidsperiode().tom()).isNotNull()
            .isAfter(frilansoppdrag.tidsperiode().fom());

        var selvstendigNæringsdrivendeInfo = søker.selvstendigNæringsdrivendeInformasjon();
        assertThat(selvstendigNæringsdrivendeInfo).hasSize(1);
        var selvstendigNæringsdrivendeInformasjon = selvstendigNæringsdrivendeInfo.get(0);
        assertThat(selvstendigNæringsdrivendeInformasjon.næringstyper()).hasSize(1);
        assertThat(selvstendigNæringsdrivendeInformasjon.tidsperiode()).isNotNull();
        assertThat(selvstendigNæringsdrivendeInformasjon.tidsperiode().fom()).isNotNull();
        assertThat(selvstendigNæringsdrivendeInformasjon.tidsperiode().tom()).isNull();
        assertThat(selvstendigNæringsdrivendeInformasjon.organisasjonsnummer()).isEqualTo("999999999");
        assertThat(selvstendigNæringsdrivendeInformasjon.harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene()).isFalse();
        assertThat(selvstendigNæringsdrivendeInformasjon.regnskapsfører()).isNotNull();
        assertThat(selvstendigNæringsdrivendeInformasjon.næringsinntekt()).isEqualTo(220_000);
        assertThat(selvstendigNæringsdrivendeInformasjon.næringstyper()).hasSize(1).contains("DAGMAMMA");

        var barn = sf.getBarn();
        assertThat(barn).isNotNull();
        assertThat(barn.fødselsdatoer()).hasSize(1);
        assertThat(barn.antallBarn()).isEqualTo(1);
        assertThat(barn.termindato()).isNotNull();
        assertThat(barn.terminbekreftelse()).isEmpty();
        assertThat(barn.adopsjonsvedtak()).isEmpty();
        assertThat(barn.omsorgsovertakelse()).isEmpty();
        assertThat(barn.dokumentasjonAvAleneomsorg()).isEmpty();

        var annenForelder = sf.getAnnenForelder();
        assertThat(annenForelder).isNotNull();
        assertThat(annenForelder.kanIkkeOppgis()).isFalse();
        assertThat(annenForelder.fornavn()).isNotNull();
        assertThat(annenForelder.etternavn()).isNotNull();
        assertThat(annenForelder.fnr()).isEqualTo("11111122222");

        var informasjonOmUtenlandsopphold = sf.getInformasjonOmUtenlandsopphold();
        assertThat(informasjonOmUtenlandsopphold).isNotNull();
        assertThat(informasjonOmUtenlandsopphold.tidligereOpphold()).isEmpty();
        assertThat(informasjonOmUtenlandsopphold.senereOpphold()).hasSize(1);
        assertThat(informasjonOmUtenlandsopphold.senereOpphold().get(0).land()).isEqualTo("FI");
        assertThat(informasjonOmUtenlandsopphold.senereOpphold().get(0).tidsperiode().fom()).isNotNull();
        assertThat(informasjonOmUtenlandsopphold.senereOpphold().get(0).tidsperiode().tom()).isNotNull()
            .isAfter(informasjonOmUtenlandsopphold.senereOpphold().get(0).tidsperiode().fom());

        assertThat(sf.getVedlegg()).isEmpty();
    }

    @Test
    void ettersendelseSeraliseringVirkerTest() throws IOException {
        var ettersendelse = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), no.nav.foreldrepenger.selvbetjening.innsending.domain.Ettersending.class);

        assertThat(ettersendelse.type()).isEqualTo("foreldrepenger");
        assertThat(ettersendelse.saksnummer()).isEqualTo("352003201");

        assertThat(ettersendelse.vedlegg()).hasSize(1);
        var vedlegg = ettersendelse.vedlegg().get(0);
        assertThat(vedlegg.getId()).isEqualTo("V090740687265315217194125674862219730");
        assertThat(vedlegg.getSkjemanummer()).isEqualTo("I000044");
        assertThat(vedlegg.getContent()).isNull();
        assertThat(vedlegg.getInnsendingsType()).isNull();
    }
}
