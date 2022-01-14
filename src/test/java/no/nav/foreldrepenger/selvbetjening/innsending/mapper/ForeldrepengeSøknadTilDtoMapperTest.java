package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
import no.nav.foreldrepenger.common.domain.felles.opptjening.NorskOrganisasjon;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Adopsjon;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.FremtidigFødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Fødsel;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Endringssøknad;
import no.nav.foreldrepenger.common.domain.foreldrepenger.Foreldrepenger;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.GradertUttaksPeriode;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.LukketPeriodeMedVedlegg;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.UtsettelsesPeriode;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.InnsendingConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.ForeldrepengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JacksonConfiguration.class, ForeldrepengeSøknadTilDtoMapperTest.InnsendingConnectionConfiguration.class})
class ForeldrepengeSøknadTilDtoMapperTest {

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
    void foreldrepengerAdopsjonAnnenInntektTest() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), SøknadFrontend.class);
        assertThat(sf).isNotNull().isInstanceOf(ForeldrepengesøknadFrontend.class);
        assertThat(sf.getSaksnummer()).isNull();

        var søknad = connection.body(sf);
        assertThat(søknad).isNotNull();

        var søker = søknad.getSøker();
        assertThat(søker.getSøknadsRolle()).isEqualTo(BrukerRolle.MOR);
        assertThat(søker.getMålform()).isEqualTo(Målform.NB);

        assertThat(søknad.getYtelse()).isInstanceOf(Foreldrepenger.class);
        var ytelse = (Foreldrepenger) søknad.getYtelse();
        assertThat(ytelse.getRelasjonTilBarn()).isInstanceOf(Adopsjon.class);
        var annenOpptjening = ytelse.getOpptjening().getAnnenOpptjening();
        assertThat(annenOpptjening).hasSize(1);
        assertThat(annenOpptjening.get(0).getType().name()).isEqualTo(sf.getSøker().andreInntekterSiste10Mnd().get(0).type());
        var periode = annenOpptjening.get(0).getPeriode();
        assertThat(periode.fom()).isNotNull();
        assertThat(periode.tom()).isNotNull();
        assertThat(periode.fom()).isBefore(periode.tom());
    }

    @Test
    void farSøkerForeldrepengerMedGraderingPåAlleUttaksPeriodeneOgFrilans() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_far_gardering_frilans.json"), SøknadFrontend.class);
        assertThat(sf).isNotNull().isInstanceOf(ForeldrepengesøknadFrontend.class);
        assertThat(sf.getSaksnummer()).isNull();

        var søknad = connection.body(sf);
        assertThat(søknad).isNotNull();
        assertThat(søknad.getSøker().getSøknadsRolle()).isEqualTo(BrukerRolle.FAR);

        var søker = søknad.getSøker();
        assertThat(søker.getSøknadsRolle()).isEqualTo(BrukerRolle.FAR);
        assertThat(søker.getMålform()).isEqualTo(Målform.NB);

        assertThat(søknad.getYtelse()).isInstanceOf(Foreldrepenger.class);
        var ytelse = (Foreldrepenger) søknad.getYtelse();
        assertThat(ytelse.getRelasjonTilBarn()).isInstanceOf(Fødsel.class);
        var uttaksperioder = ytelse.getFordeling().getPerioder();

        var gradertUttaksperioder = getGradertUttaksperioder(uttaksperioder);
        assertThat(gradertUttaksperioder).hasSize(1);
        var gradertUttaksPeriode = gradertUttaksperioder.get(0);
        assertThat(gradertUttaksPeriode.getArbeidstidProsent()).isEqualTo(new ProsentAndel(50));
        assertThat(gradertUttaksPeriode.getUttaksperiodeType().name()).isEqualToIgnoringCase("FEDREKVOTE");
        assertThat(gradertUttaksPeriode.getFrilans()).isTrue();

        var frilans = ytelse.getOpptjening().getFrilans();
        assertThat(frilans).isNotNull();
        assertThat(frilans.isHarInntektFraFosterhjem()).isFalse();
        assertThat(frilans.isJobberFremdelesSomFrilans()).isTrue();
        assertThat(frilans.getPeriode().fom()).isNotNull();
        assertThat(frilans.getPeriode().tom()).isNull();
        assertThat(frilans.getFrilansOppdrag()).isEmpty();

    }


    @Test
    void søknadMappesTilIkkeTomtObjektOgUttakMappesKorrektTilGradertUttak() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_gradering_egenNæring_og_frilans.json"), SøknadFrontend.class);
        assertThat(sf).isNotNull().isInstanceOf(ForeldrepengesøknadFrontend.class);

        var søknad = connection.body(sf);
        assertThat(søknad).isNotNull();
        assertThat(søknad.getSøker().getSøknadsRolle()).isEqualTo(BrukerRolle.MOR);
        assertThat(søknad.getYtelse()).isInstanceOf(Foreldrepenger.class);
        var ytelse = (Foreldrepenger) søknad.getYtelse();
        assertThat(ytelse.getRelasjonTilBarn()).isInstanceOf(Fødsel.class);
        var uttaksperioder = ytelse.getFordeling().getPerioder();

        var gradertUttaksperioder = getGradertUttaksperioder(uttaksperioder);
        assertThat(gradertUttaksperioder).hasSize(1);
        var gradertUttaksPeriode = gradertUttaksperioder.get(0);
        assertThat(gradertUttaksPeriode.getArbeidstidProsent()).isEqualTo(new ProsentAndel(45));
        assertThat(gradertUttaksPeriode.getUttaksperiodeType().name()).isEqualToIgnoringCase("FORELDREPENGER");
        assertThat(gradertUttaksPeriode.getFrilans()).isFalse();
        assertThat(gradertUttaksPeriode.getSelvstendig()).isFalse();
        assertThat(gradertUttaksPeriode.isErArbeidstaker()).isTrue();

        var frilans = ytelse.getOpptjening().getFrilans();
        assertThat(frilans).isNotNull();
        assertThat(frilans.isHarInntektFraFosterhjem()).isFalse();
        assertThat(frilans.isJobberFremdelesSomFrilans()).isTrue();
        assertThat(frilans.getPeriode().fom()).isNotNull();
        assertThat(frilans.getPeriode().tom()).isNull();
        assertThat(frilans.getFrilansOppdrag()).hasSize(1);
        var frilansoppdrag = frilans.getFrilansOppdrag().get(0);
        assertThat(frilansoppdrag.oppdragsgiver()).isEqualTo("Klara Klukk");
        assertThat(frilansoppdrag.periode().fom()).isNotNull();
        assertThat(frilansoppdrag.periode().tom()).isNotNull();

        var egennæringer = ytelse.getOpptjening().getEgenNæring();
        assertThat(egennæringer).hasSize(1);
        var egennæring = egennæringer.get(0);
        assertThat(egennæring).isInstanceOf(NorskOrganisasjon.class);
        var norskOrganisasjon = (NorskOrganisasjon) egennæring;
        assertThat(norskOrganisasjon.getOrgName()).isEqualTo("Barnehage V2");
        assertThat(norskOrganisasjon.getOrgNummer()).isNotNull();
        assertThat(norskOrganisasjon.getRegnskapsførere()).hasSize(1);
        assertThat(norskOrganisasjon.isNærRelasjon()).isTrue();
        assertThat(norskOrganisasjon.getNæringsinntektBrutto()).isEqualTo(220_000);
        assertThat(norskOrganisasjon.isErNyIArbeidslivet()).isFalse();
        assertThat(norskOrganisasjon.isErNyOpprettet()).isTrue();
    }


    @Test
    void søknadMedUtsettelseOgUtenlandsoppholdMappesKorrekt() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_utsettelse_og_utenlandsopphold.json"), SøknadFrontend.class);
        assertThat(sf).isNotNull().isInstanceOf(ForeldrepengesøknadFrontend.class);

        var søknad = connection.body(sf);
        assertThat(søknad).isNotNull();
        assertThat(søknad.getSøker().getSøknadsRolle()).isEqualTo(BrukerRolle.MOR);
        assertThat(søknad.getYtelse()).isInstanceOf(Foreldrepenger.class);
        var ytelse = (Foreldrepenger) søknad.getYtelse();
        assertThat(ytelse.getRelasjonTilBarn()).isInstanceOf(Fødsel.class);
        var uttaksperioder = ytelse.getFordeling().getPerioder();

        var utsettelsesPerioder = getUtsettelsesPeriode(uttaksperioder);
        assertThat(utsettelsesPerioder).hasSize(1);
        var utsettelseperiode = utsettelsesPerioder.get(0);
        assertThat(utsettelseperiode.getUttaksperiodeType()).isNull();
        assertThat(utsettelseperiode.getÅrsak().name()).isEqualTo("SYKDOM");
        assertThat(utsettelseperiode.getUttaksperiodeType()).isNull();
        assertThat(utsettelseperiode.getFom()).isNotNull();
        assertThat(utsettelseperiode.getTom()).isNotNull();

        var frilans = ytelse.getOpptjening().getFrilans();
        assertThat(frilans).isNotNull();
        assertThat(frilans.isHarInntektFraFosterhjem()).isFalse();
        assertThat(frilans.isJobberFremdelesSomFrilans()).isTrue();
        assertThat(frilans.getPeriode().fom()).isNotNull();
        assertThat(frilans.getPeriode().tom()).isNull();
        assertThat(frilans.getFrilansOppdrag()).isEmpty();

        assertThat(ytelse.getOpptjening().getEgenNæring()).isEmpty();
        assertThat(ytelse.getOpptjening().getUtenlandskArbeidsforhold()).isEmpty();
        assertThat(ytelse.getOpptjening().getAnnenOpptjening()).isEmpty();
    }

    @Test
    void endringsSøknadMapperTest() throws IOException {
        var sf = mapper.readValue(bytesFra("json/endringssøknad_termin_mor.json"), SøknadFrontend.class);
        assertThat(sf).isNotNull().isInstanceOf(ForeldrepengesøknadFrontend.class);
        assertThat(sf.getSaksnummer()).isNotNull();

        var søknad = connection.body(sf);
        assertThat(søknad)
            .isNotNull()
            .isInstanceOf(Endringssøknad.class);

        assertThat(søknad.getYtelse()).isInstanceOf(Foreldrepenger.class);
        var ytelse = (Foreldrepenger) søknad.getYtelse();
        assertThat(ytelse.getRelasjonTilBarn()).isInstanceOf(FremtidigFødsel.class);
    }


    private List<UtsettelsesPeriode> getUtsettelsesPeriode(List<LukketPeriodeMedVedlegg> uttaksperioder) {
        return uttaksperioder.stream()
            .filter(perioder -> perioder instanceof UtsettelsesPeriode)
            .map(periode -> (UtsettelsesPeriode) periode)
            .toList();
    }


    private List<GradertUttaksPeriode> getGradertUttaksperioder(List<LukketPeriodeMedVedlegg> uttaksperioder) {
        return uttaksperioder.stream()
            .filter(perioder -> perioder instanceof GradertUttaksPeriode)
            .map(periode -> (GradertUttaksPeriode) periode)
            .toList();
    }

}
