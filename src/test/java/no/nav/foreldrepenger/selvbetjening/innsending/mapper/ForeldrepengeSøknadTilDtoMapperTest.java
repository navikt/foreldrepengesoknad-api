package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.i18n.CountryCode;
import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.felles.ProsentAndel;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class ForeldrepengeSøknadTilDtoMapperTest {

    private final InnsendingConnection connection = new InnsendingConnection(null, null, new Image2PDFConverter());

    @Autowired
    private ObjectMapper mapper;


    @Test
    void foreldrepengerAdopsjonAnnenInntektTest() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_adopsjon_annenInntekt.json"), SøknadFrontend.class);
        assertThat(sf).isNotNull().isInstanceOf(ForeldrepengesøknadFrontend.class);
        assertThat(sf.getSaksnummer()).isNull();

        var søknad = connection.body(sf);
        assertThat(søknad).isNotNull();

        var søker = søknad.getSøker();
        assertThat(søker.søknadsRolle()).isEqualTo(BrukerRolle.MOR);
        assertThat(søker.målform()).isEqualTo(Målform.NB);

        assertThat(søknad.getYtelse()).isInstanceOf(Foreldrepenger.class);
        var ytelse = (Foreldrepenger) søknad.getYtelse();
        assertThat(ytelse.relasjonTilBarn()).isInstanceOf(Adopsjon.class);
        var annenOpptjening = ytelse.opptjening().annenOpptjening();
        assertThat(annenOpptjening).hasSize(1);
        assertThat(annenOpptjening.get(0).type().name()).isEqualTo(sf.getSøker().andreInntekterSiste10Mnd().get(0).type());
        var periode = annenOpptjening.get(0).periode();
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
        assertThat(søknad.getSøker().søknadsRolle()).isEqualTo(BrukerRolle.FAR);

        var søker = søknad.getSøker();
        assertThat(søker.søknadsRolle()).isEqualTo(BrukerRolle.FAR);
        assertThat(søker.målform()).isEqualTo(Målform.NB);

        assertThat(søknad.getYtelse()).isInstanceOf(Foreldrepenger.class);
        var ytelse = (Foreldrepenger) søknad.getYtelse();
        assertThat(ytelse.relasjonTilBarn()).isInstanceOf(Fødsel.class);
        var uttaksperioder = ytelse.fordeling().perioder();

        var gradertUttaksperioder = getGradertUttaksperioder(uttaksperioder);
        assertThat(gradertUttaksperioder).hasSize(1);
        var gradertUttaksPeriode = gradertUttaksperioder.get(0);
        assertThat(gradertUttaksPeriode.getArbeidstidProsent()).isEqualTo(ProsentAndel.valueOf(50));
        assertThat(gradertUttaksPeriode.getUttaksperiodeType().name()).isEqualToIgnoringCase("FEDREKVOTE");
        assertThat(gradertUttaksPeriode.getFrilans()).isTrue();

        var frilans = ytelse.opptjening().frilans();
        assertThat(frilans).isNotNull();
        assertThat(frilans.harInntektFraFosterhjem()).isFalse();
        assertThat(frilans.jobberFremdelesSomFrilans()).isTrue();
        assertThat(frilans.periode().fom()).isNotNull();
        assertThat(frilans.periode().tom()).isNull();
        assertThat(frilans.frilansOppdrag()).isNotNull();
        var frilansOppdrag = frilans.frilansOppdrag().get(0);
        assertThat(frilansOppdrag.oppdragsgiver()).isNotNull();
        assertThat(frilansOppdrag.periode().fom()).isNotNull();
        assertThat(frilansOppdrag.periode().tom()).isNull();

    }


    @Test
    void søknadMappesTilIkkeTomtObjektOgUttakMappesKorrektTilGradertUttak() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_gradering_egenNæring_og_frilans.json"), SøknadFrontend.class);
        assertThat(sf).isNotNull().isInstanceOf(ForeldrepengesøknadFrontend.class);

        var søknad = connection.body(sf);
        assertThat(søknad).isNotNull();
        assertThat(søknad.getSøker().søknadsRolle()).isEqualTo(BrukerRolle.MOR);
        assertThat(søknad.getYtelse()).isInstanceOf(Foreldrepenger.class);
        var ytelse = (Foreldrepenger) søknad.getYtelse();
        assertThat(ytelse.relasjonTilBarn()).isInstanceOf(Fødsel.class);
        var uttaksperioder = ytelse.fordeling().perioder();

        var gradertUttaksperioder = getGradertUttaksperioder(uttaksperioder);
        assertThat(gradertUttaksperioder).hasSize(1);
        var gradertUttaksPeriode = gradertUttaksperioder.get(0);
        assertThat(gradertUttaksPeriode.getArbeidstidProsent()).isEqualTo(ProsentAndel.valueOf(45));
        assertThat(gradertUttaksPeriode.getUttaksperiodeType().name()).isEqualToIgnoringCase("FORELDREPENGER");
        assertThat(gradertUttaksPeriode.getFrilans()).isFalse();
        assertThat(gradertUttaksPeriode.getSelvstendig()).isFalse();
        assertThat(gradertUttaksPeriode.isErArbeidstaker()).isTrue();

        var frilans = ytelse.opptjening().frilans();
        assertThat(frilans).isNotNull();
        assertThat(frilans.harInntektFraFosterhjem()).isFalse();
        assertThat(frilans.jobberFremdelesSomFrilans()).isTrue();
        assertThat(frilans.periode().fom()).isNotNull();
        assertThat(frilans.periode().tom()).isNull();
        assertThat(frilans.frilansOppdrag()).hasSize(1);
        var frilansoppdrag = frilans.frilansOppdrag().get(0);
        assertThat(frilansoppdrag.oppdragsgiver()).isEqualTo("Klara Klukk");
        assertThat(frilansoppdrag.periode().fom()).isNotNull();
        assertThat(frilansoppdrag.periode().tom()).isNotNull();

        var egennæringer = ytelse.opptjening().egenNæring();
        assertThat(egennæringer).hasSize(1);
        var egennæring = egennæringer.get(0);
        assertThat(egennæring.registrertILand()).isEqualTo(CountryCode.NO);
        assertThat(egennæring.orgName()).isNotNull();
        assertThat(egennæring.orgNummer()).isNotNull();
        assertThat(egennæring.regnskapsførere()).hasSize(1);
        assertThat(egennæring.nærRelasjon()).isTrue();
        assertThat(egennæring.næringsinntektBrutto()).isEqualTo(220_000);
        assertThat(egennæring.erNyIArbeidslivet()).isFalse();
        assertThat(egennæring.erNyOpprettet()).isTrue();
    }


    @Test
    void søknadMedUtsettelseOgUtenlandsoppholdMappesKorrekt() throws IOException {
        var sf = mapper.readValue(bytesFra("json/foreldrepenger_mor_utsettelse_og_utenlandsopphold.json"), SøknadFrontend.class);
        assertThat(sf).isNotNull().isInstanceOf(ForeldrepengesøknadFrontend.class);

        var søknad = connection.body(sf);
        assertThat(søknad).isNotNull();
        assertThat(søknad.getSøker().søknadsRolle()).isEqualTo(BrukerRolle.MOR);
        assertThat(søknad.getYtelse()).isInstanceOf(Foreldrepenger.class);
        var ytelse = (Foreldrepenger) søknad.getYtelse();
        assertThat(ytelse.relasjonTilBarn()).isInstanceOf(Fødsel.class);
        var uttaksperioder = ytelse.fordeling().perioder();

        var utsettelsesPerioder = getUtsettelsesPeriode(uttaksperioder);
        assertThat(utsettelsesPerioder).hasSize(1);
        var utsettelseperiode = utsettelsesPerioder.get(0);
        assertThat(utsettelseperiode.getÅrsak().name()).isEqualTo("SYKDOM");
        assertThat(utsettelseperiode.getFom()).isNotNull();
        assertThat(utsettelseperiode.getTom()).isNotNull();

        var opptjening = ytelse.opptjening();
        var frilans = opptjening.frilans();
        assertThat(frilans).isNotNull();
        assertThat(frilans.harInntektFraFosterhjem()).isFalse();
        assertThat(frilans.jobberFremdelesSomFrilans()).isTrue();
        assertThat(frilans.periode().fom()).isNotNull();
        assertThat(frilans.periode().tom()).isNull();
        assertThat(frilans.frilansOppdrag()).isEmpty();

        assertThat(opptjening.egenNæring()).isEmpty();
        assertThat(opptjening.utenlandskArbeidsforhold()).isEmpty();
        assertThat(opptjening.annenOpptjening()).isEmpty();
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
        assertThat(ytelse.relasjonTilBarn()).isInstanceOf(FremtidigFødsel.class);
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
