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
}
