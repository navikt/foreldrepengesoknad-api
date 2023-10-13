package no.nav.foreldrepenger.selvbetjening.innsending.deseralisering;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static no.nav.foreldrepenger.selvbetjening.innsending.dto.ettersendelse.YtelseType.FORELDREPENGER;
import static no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger.UttaksperiodeType.UTTAK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.innsending.dto.foreldrepenger.Situasjon;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
class SøknadFrontendDeseraliseringTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private ObjectMapper mapper;

    // TODO: Legg til en for SVP, ES og FP endring
    @Test
    void innsendtSøknadFraFrontendDeseraliseresKorrektTest() throws IOException {
        var søknadFrontend = mapper.readValue(bytesFra("json/foreldrepenger_mor_gradering_egenNæring_og_frilans.json"), SøknadDto.class);

        // Verifiser korrekt seralisering fra frontend
        assertThat(søknadFrontend).isInstanceOf(ForeldrepengesøknadDto.class);
        var fs = (ForeldrepengesøknadDto) søknadFrontend;
        assertThat(fs.type()).isEqualTo("foreldrepenger");
        assertThat(fs.situasjon()).isEqualTo(Situasjon.FØDSEL);
        assertThat(fs.dekningsgrad()).isEqualTo(Dekningsgrad.ÅTTI);
        assertThat(fs.ønskerJustertUttakVedFødsel()).isTrue();

        assertThat(fs.uttaksplan()).hasSize(3);
        var uttaksperiode1 = fs.uttaksplan().get(0);
        assertThat(uttaksperiode1.forelder()).isEqualTo("mor");
        assertThat(uttaksperiode1.type()).isEqualTo(UTTAK);
        assertThat(uttaksperiode1.konto()).isEqualTo("FORELDREPENGER_FØR_FØDSEL");
        assertThat(uttaksperiode1.gradert()).isFalse();
        assertThat(uttaksperiode1.ønskerSamtidigUttak()).isFalse();
        assertThat(uttaksperiode1.tidsperiode()).isNotNull();
        assertThat(uttaksperiode1.tidsperiode().fom()).isNotNull();
        assertThat(uttaksperiode1.tidsperiode().tom()).isNotNull()
            .isAfter(uttaksperiode1.tidsperiode().fom());
        assertThat(uttaksperiode1.forelder()).isEqualTo("mor");

        var uttaksperiode2 = fs.uttaksplan().get(1);
        assertThat(uttaksperiode2.forelder()).isEqualTo("mor");
        assertThat(uttaksperiode2.type()).isEqualTo(UTTAK);
        assertThat(uttaksperiode2.konto()).isEqualTo("FORELDREPENGER");
        assertThat(uttaksperiode2.gradert()).isFalse();
        assertThat(uttaksperiode2.tidsperiode()).isNotNull();
        assertThat(uttaksperiode2.tidsperiode().fom()).isNotNull();
        assertThat(uttaksperiode2.tidsperiode().tom()).isNotNull()
            .isAfter(uttaksperiode2.tidsperiode().fom());

        var uttaksperiode3 = fs.uttaksplan().get(2);
        assertThat(uttaksperiode3.forelder()).isEqualTo("mor");
        assertThat(uttaksperiode3.type()).isEqualTo(UTTAK);
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


        var søker = fs.søker();
        assertThat(søker).isNotNull();
        assertThat(søker.rolle()).isEqualTo(BrukerRolle.MOR);
        assertThat(søker.språkkode()).isEqualTo(Målform.NB);
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
        assertThat(selvstendigNæringsdrivendeInformasjon.organisasjonsnummer()).isNotNull();
        assertThat(selvstendigNæringsdrivendeInformasjon.harBlittYrkesaktivILøpetAvDeTreSisteFerdigliknedeÅrene()).isFalse();
        assertThat(selvstendigNæringsdrivendeInformasjon.regnskapsfører()).isNotNull();
        assertThat(selvstendigNæringsdrivendeInformasjon.næringsinntekt()).isEqualTo(220_000);
        assertThat(selvstendigNæringsdrivendeInformasjon.næringstyper()).hasSize(1).contains(Virksomhetstype.DAGMAMMA);

        var barn = søknadFrontend.barn();
        assertThat(barn).isNotNull();
        assertThat(barn.fødselsdatoer()).hasSize(1);
        assertThat(barn.antallBarn()).isEqualTo(1);
        assertThat(barn.termindato()).isNotNull();
        assertThat(barn.terminbekreftelse()).isEmpty();
        assertThat(barn.adopsjonsvedtak()).isEmpty();
        assertThat(barn.omsorgsovertakelse()).isEmpty();
        assertThat(barn.dokumentasjonAvAleneomsorg()).isEmpty();

        var annenForelder = fs.annenForelder();
        assertThat(annenForelder).isNotNull();
        assertThat(annenForelder.kanIkkeOppgis()).isFalse();
        assertThat(annenForelder.fornavn()).isNotNull();
        assertThat(annenForelder.etternavn()).isNotNull();
        assertThat(annenForelder.fnr()).isEqualTo("11111122222");

        var informasjonOmUtenlandsopphold = søknadFrontend.informasjonOmUtenlandsopphold();
        assertThat(informasjonOmUtenlandsopphold).isNotNull();
        assertThat(informasjonOmUtenlandsopphold.tidligereOpphold()).isEmpty();
        assertThat(informasjonOmUtenlandsopphold.senereOpphold()).hasSize(1);
        assertThat(informasjonOmUtenlandsopphold.senereOpphold().get(0).land()).isEqualTo("FI");
        assertThat(informasjonOmUtenlandsopphold.senereOpphold().get(0).tidsperiode().fom()).isNotNull();
        assertThat(informasjonOmUtenlandsopphold.senereOpphold().get(0).tidsperiode().tom()).isNotNull()
            .isAfter(informasjonOmUtenlandsopphold.senereOpphold().get(0).tidsperiode().fom());

        assertThat(søknadFrontend.vedlegg()).isEmpty();
    }

    @Test
    void ettersendelseDeraliseringTest() throws IOException, URISyntaxException {
        var ettersendelse = mapper.readValue(bytesFra("json/ettersendelse_I000044.json"), EttersendelseDto.class);
        assertThat(ettersendelse).isNotNull();
        assertThat(ettersendelse.saksnummer().value()).isEqualTo("352003201");
        assertThat(ettersendelse.type()).isEqualTo(FORELDREPENGER);

        var vedleggListe = ettersendelse.vedlegg();
        assertThat(vedleggListe).hasSize(1);
        var vedlegg = vedleggListe.get(0);
        assertThat(vedlegg.getSkjemanummer()).isEqualTo("I000044");
        assertThat(vedlegg.getId()).isEqualTo(new MutableVedleggReferanseDto("V090740687265315217194125674862219730"));
        assertThat(vedlegg.getUrl()).isEqualTo(new URI("https://foreldrepengesoknad-api.intern.dev.nav.no/rest/storage/vedlegg/b9974360-6c07-4b9d-acac-14f0f417d200"));
    }

    @Test
    void innsendingVedlegglistestørrelsevalidatorTest() {
        var forMangeSendSenere = validVedleggsliste(101, 0);
        assertFalse(forMangeSendSenere);

        var forMangeOpplastede = validVedleggsliste(0, 41);
        assertFalse(forMangeOpplastede);

        var innenforGrensene = validVedleggsliste(50, 35);
        assertTrue(innenforGrensene);

        var tomListeInnenforGrensen = validVedleggsliste(0, 0);
        assertTrue(tomListeInnenforGrensen);
    }

    private boolean validVedleggsliste(int sendSenereVedlegg, int opplastetVedlegg) {
        List<VedleggDto>  sendSenere = new ArrayList<>();

        while (sendSenere.size() < sendSenereVedlegg) {
            var nyttVedlegg = new VedleggDto(null, "Beskrivelse", new MutableVedleggReferanseDto("Id"), "SEND_SENERE", "Skjemanummer", "xyz", null,
                null);
            sendSenere.add(nyttVedlegg);
        }

        List<VedleggDto> opplastet = new ArrayList<>();
        while (opplastet.size() < opplastetVedlegg) {
            var nyttVedlegg = new VedleggDto(null, "Beskrivelse", new MutableVedleggReferanseDto("Id"), null, "Skjemanummer", "xyz", null, 123);
            opplastet.add(nyttVedlegg);
        }
        sendSenere.addAll(opplastet);

        var søknad = new ForeldrepengesøknadDto(null, null,
            null, null, null, "", null,
            null, null, sendSenere);

        var constraintViolations = validator.validate(søknad);
        var match = constraintViolations.stream().anyMatch(cv -> cv.getMessageTemplate().equals("Vedleggslisten kan ikke inneholde flere enn 40 opplastede vedlegg eller 100 vedlegg som skal sendes senere."));
        return !match;
    }
}
