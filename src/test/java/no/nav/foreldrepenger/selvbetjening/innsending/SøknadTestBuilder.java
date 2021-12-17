package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.AnnenForelder;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Barn;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søker;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Tidsperiode;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Utenlandsopphold;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.UtenlandsoppholdPeriode;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.UttaksplanPeriode;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.arbeid.FrilansInformasjon;

// TODO: Fjern denne.
public final class SøknadTestBuilder {


    private static final String VEDLEGG_ID = "V0477015220683470502664423844138226544";

    public static Foreldrepengesøknad foreldrepengesøknad() {
        var søknad = new Foreldrepengesøknad();
        søknad.setType("foreldrepenger"); // TODO: Trenger ikke
        søknad.setDekningsgrad("100");
        søknad.setUttaksplan(uttaksplan());

        // generell søknad
        søknad.setSøker(søker("MOR"));
        søknad.setBarn(barn(LocalDate.now().minusDays(5)));
        søknad.setAnnenForelder(annenforelder("22222233333"));
        søknad.setInformasjonOmUtenlandsopphold(informasjonOmUtenlandsopphold());
        // TODO: Sendes ned, men mappes ikke
        // harGodkjentVilkår
        // harGodkjentOppsummering
        søknad.setErEndringssøknad(false);
        søknad.setVedlegg(vedlegg());
        return søknad;
    }

    private static List<Vedlegg> vedlegg() {
        var vedlegg = new Vedlegg();
        vedlegg.setId(VEDLEGG_ID);
        vedlegg.setInnsendingsType("SEND_SENERE");
        vedlegg.setSkjemanummer("I000023");
        // TODO: Sendes ned, men mappes ikke
        // file
        // filename
        // filesize
        // uploaded
        // pending
        // type
        return List.of(vedlegg);
    }

    private static Utenlandsopphold informasjonOmUtenlandsopphold() {
        var utenlandsopphold = new Utenlandsopphold();
        // TODO: Sendes ned, men mappes ikke
        // iNorgeNeste12Mnd
        // iNorgeSiste12Mnd
        utenlandsopphold.setSenereOpphold(senereOpphold());
        utenlandsopphold.setTidligereOpphold(emptyList());
        return utenlandsopphold;
    }

    private static List<UtenlandsoppholdPeriode> senereOpphold() {
        var utenlandsoppholdPeriode = new UtenlandsoppholdPeriode();
        utenlandsoppholdPeriode.setLand("GB");
        var tidsperiode = new Tidsperiode();
        tidsperiode.setTom(LocalDate.now().plusMonths(2));
        tidsperiode.setFom(LocalDate.now().plusMonths(3));
        utenlandsoppholdPeriode.setTidsperiode(tidsperiode);
        return List.of(utenlandsoppholdPeriode);
    }

    private static AnnenForelder annenforelder(String fnr) {
        return new AnnenForelder(
            false,
            "DØLL",
            "GYNGEHEST",
            null,
            fnr,
            null,
            null,
            true,
            true,
            null,
            null
        );
    }

    private static Barn barn(LocalDate fødselsdato) {
        var barn = new Barn();
        barn.antallBarn = 1;
        barn.erBarnetFødt = true;
        barn.fødselsdatoer = List.of(fødselsdato);
        barn.termindato = fødselsdato.minusDays(3);
        return barn;
    }

    private static Søker søker(String rolle) {
        var søker = new Søker();
        søker.setRolle(rolle);
        søker.setSpråkkode("NB");
        søker.setErAleneOmOmsorg(false);

        // søker.setharJobbetSomFrilansSiste10Mnd(true) TODO: Sendes ned, men blir ikke brukt.
        var frilansInformasjon = new FrilansInformasjon();
        frilansInformasjon.setOppstart(LocalDate.now().minusYears(1));
        frilansInformasjon.setDriverFosterhjem(false);
        // TODO: Sendes ned, men mappes ikke
        // jobberFremdelesSomFrilans
        // harJobbetForNærVennEllerFamilieSiste10Mnd
        frilansInformasjon.setOppdragForNæreVennerEllerFamilieSiste10Mnd(emptyList());
        søker.setFrilansInformasjon(frilansInformasjon);

        søker.setSelvstendigNæringsdrivendeInformasjon(emptyList());
        søker.setAndreInntekterSiste10Mnd(emptyList());
        return søker;
    }

    private static List<UttaksplanPeriode> uttaksplan() {
        return List.of(
            uttaksperiodeFFF(LocalDate.now().minusWeeks(3), LocalDate.now().minusDays(1)),
            uttaksperiodeSykdom(LocalDate.now(), LocalDate.now().plusWeeks(1).minusDays(1)),
            uttaksperiodeMødrekvote(LocalDate.now().plusWeeks(1), LocalDate.now().plusWeeks(10).minusDays(1)),
            uttaksperiodeFellesperiode(LocalDate.now().plusWeeks(12), LocalDate.now().plusWeeks(12).minusDays(1))
        );
    }

    private static UttaksplanPeriode uttaksperiodeFellesperiode(LocalDate fom, LocalDate tom) {
        var periode = new UttaksplanPeriode();
        periode.setType("uttak");
        periode.setKonto("FELLESPERIODE");
        periode.setForelder("mor");
        periode.setGradert(false);
        periode.setØnskerSamtidigUttak(false);
        setTidsperiode(fom, tom, periode);
        return periode;
    }

    private static UttaksplanPeriode uttaksperiodeMødrekvote(LocalDate fom, LocalDate tom) {
        var periode = new UttaksplanPeriode();
        periode.setType("uttak");
        periode.setKonto("MØDREKVOTE");
        periode.setForelder("mor");
        periode.setGradert(false);
        periode.setØnskerSamtidigUttak(false);
        setTidsperiode(fom, tom, periode);
        return periode;
    }

    private static UttaksplanPeriode uttaksperiodeMødrekvoteGradert(LocalDate fom, LocalDate tom) {
        var periode = new UttaksplanPeriode();
        periode.setType("uttak");
        periode.setKonto("MØDREKVOTE");
        periode.setForelder("mor");
        periode.setGradert(false);
        periode.setØnskerSamtidigUttak(false);
        setTidsperiode(fom, tom, periode);
        return periode;
    }


    private static UttaksplanPeriode uttaksperiodeSykdom(LocalDate fom, LocalDate tom) {
        var periode = new UttaksplanPeriode();
        periode.setType("utsettelse");
        periode.setÅrsak("SYKDOM");
        periode.setForelder("mor");
        periode.setErArbeidstaker(true);
        periode.setVedlegg(List.of(VEDLEGG_ID));
        setTidsperiode(fom, tom, periode);
        return periode;
    }

    private static UttaksplanPeriode uttaksperiodeFFF(LocalDate fom, LocalDate tom) {
        var periode = new UttaksplanPeriode();
        periode.setType("uttak");
        periode.setKonto("FORELDREPENGER_FØR_FØDSEL");
        periode.setForelder("mor");
        setTidsperiode(fom, tom, periode);
        return periode;
    }

    private static void setTidsperiode(LocalDate fom, LocalDate tom, UttaksplanPeriode periode) {
        var tidsperiode = new Tidsperiode();
        tidsperiode.setFom(ukeDagNær(fom));
        tidsperiode.setTom(ukeDagNær(tom));
        periode.setTidsperiode(tidsperiode);
    }

    private static LocalDate ukeDagNær(LocalDate dato) {
        LocalDate d = dato;
        while (!erUkedag(d)) {
            d = d.plusDays(1);
        }
        return d;
    }

    private static boolean erUkedag(LocalDate dato) {
        return !dato.getDayOfWeek().equals(SATURDAY) && !dato.getDayOfWeek().equals(SUNDAY);
    }
}
