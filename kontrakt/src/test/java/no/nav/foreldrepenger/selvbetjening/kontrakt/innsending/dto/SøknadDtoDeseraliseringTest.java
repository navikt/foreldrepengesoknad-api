package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto;

import static no.nav.foreldrepenger.common.util.ResourceHandleUtil.bytesFra;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDto.Type.UTTAK;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.felles.opptjening.Virksomhetstype;
import no.nav.foreldrepenger.common.domain.foreldrepenger.fordeling.StønadskontoType;
import no.nav.foreldrepenger.common.mapper.DefaultJsonMapper;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Dekningsgrad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.Situasjon;


class SøknadDtoDeseraliseringTest {
    private static final ObjectMapper MAPPER = DefaultJsonMapper.MAPPER;

    @Test
    void innsendtSøknadFraFrontendDeseraliseresKorrektTest() throws IOException {
        var søknadFrontend = MAPPER.readValue(bytesFra("json/foreldrepenger_mor_gradering_egenNæring_og_frilans.json"), SøknadDto.class);

        // Verifiser korrekt seralisering fra frontend
        assertThat(søknadFrontend).isInstanceOf(ForeldrepengesøknadDto.class);
        var fs = (ForeldrepengesøknadDto) søknadFrontend;
        assertThat(fs.navn()).isEqualTo("foreldrepenger");
        assertThat(fs.situasjon()).isEqualTo(Situasjon.FØDSEL);
        assertThat(fs.dekningsgrad()).isEqualTo(Dekningsgrad.ÅTTI);
        assertThat(fs.ønskerJustertUttakVedFødsel()).isTrue();

        assertThat(fs.uttaksplan()).hasSize(3);
        var uttaksperiode1 = fs.uttaksplan().get(0);
        assertThat(uttaksperiode1.forelder()).isEqualTo("mor");
        assertThat(uttaksperiode1.type()).isEqualTo(UTTAK);
        assertThat(uttaksperiode1.konto()).isEqualTo(StønadskontoType.FORELDREPENGER_FØR_FØDSEL);
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
        assertThat(uttaksperiode2.konto()).isEqualTo(StønadskontoType.FORELDREPENGER);
        assertThat(uttaksperiode2.gradert()).isFalse();
        assertThat(uttaksperiode2.tidsperiode()).isNotNull();
        assertThat(uttaksperiode2.tidsperiode().fom()).isNotNull();
        assertThat(uttaksperiode2.tidsperiode().tom()).isNotNull()
            .isAfter(uttaksperiode2.tidsperiode().fom());

        var uttaksperiode3 = fs.uttaksplan().get(2);
        assertThat(uttaksperiode3.forelder()).isEqualTo("mor");
        assertThat(uttaksperiode3.type()).isEqualTo(UTTAK);
        assertThat(uttaksperiode3.konto()).isEqualTo(StønadskontoType.FORELDREPENGER);
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
}
