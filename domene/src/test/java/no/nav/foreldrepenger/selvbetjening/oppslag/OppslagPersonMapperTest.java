package no.nav.foreldrepenger.selvbetjening.oppslag;

import static java.time.LocalDate.now;
import static no.nav.foreldrepenger.selvbetjening.oppslag.mapper.PersonMapper.tilPersonFrontend;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import no.nav.foreldrepenger.common.domain.AktørId;
import no.nav.foreldrepenger.common.domain.Barn;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Navn;
import no.nav.foreldrepenger.common.domain.felles.AnnenPart;
import no.nav.foreldrepenger.common.domain.felles.Bankkonto;
import no.nav.foreldrepenger.common.domain.felles.Kjønn;
import no.nav.foreldrepenger.common.domain.felles.Person;
import no.nav.foreldrepenger.common.domain.felles.Sivilstand;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.Arbeidsforhold;


@ExtendWith(SpringExtension.class)
class OppslagPersonMapperTest {

    private static final String ARBEIDSGIVER_ID = "123456789";
    private static final String ARBEIDSGIVER_ID_TYPE = "orgnr";
    private static final String ARBEIDSGIVER_NAVN = "KJELL T. RINGS SYKKELVERKSTED";
    private static final Double STILLINGSPROSENT = 100d;
    private static final LocalDate FOM_ARBEIDSFORHOLD = now().minusYears(2);
    private static final LocalDate TOM_ARBEIDSFORHOLD = null;


    @Test
    void personMapperMapperKorrektMedAnnenpart() {
        var personFelles = personMedAnnenpart();
        var barnFelles = personFelles.barn().iterator().next();
        var annenPartFelles = barnFelles.annenPart();

        var person = tilPersonFrontend(personFelles);
        assertThat(person.fnr()).isEqualTo(personFelles.fnr());
        assertThat(person.fornavn()).isEqualTo(personFelles.navn().fornavn());
        assertThat(person.etternavn()).isEqualTo(personFelles.navn().etternavn());
        assertThat(person.kjønn()).isEqualTo(personFelles.kjønn());
        assertThat(person.barn()).hasSize(1);
        var barn = person.barn().get(0);

        assertThat(barn.fnr()).isEqualTo(barnFelles.fnr().value());
        assertThat(barn.fødselsdato()).isEqualTo(barnFelles.fødselsdato());
        assertThat(barn.kjønn()).isEqualTo(Kjønn.K);
        var annenforelder = barn.annenForelder();
        assertThat(annenforelder).isNotNull();
        assertThat(annenforelder.fnr()).isEqualTo(annenPartFelles.fnr().value());
        assertThat(annenforelder.fødselsdato()).isEqualTo(annenPartFelles.fødselsdato());
        assertThat(annenforelder.fornavn()).isEqualTo(annenPartFelles.navn().fornavn());
        assertThat(annenforelder.mellomnavn()).isEqualTo(annenPartFelles.navn().mellomnavn());
        assertThat(annenforelder.etternavn()).isEqualTo(annenPartFelles.navn().etternavn());
    }

    @Test
    void personMapperMapperKorrektUtenAnnenpartOgToBarn() {
        var personFelles = personUtenAnnenpart();

        var person = tilPersonFrontend(personFelles);
        assertThat(person.fnr()).isEqualTo(personFelles.fnr());
        assertThat(person.fornavn()).isEqualTo(personFelles.navn().fornavn());
        assertThat(person.etternavn()).isEqualTo(personFelles.navn().etternavn());
        assertThat(person.kjønn()).isEqualTo(personFelles.kjønn());
        assertThat(person.barn()).hasSize(2);

        for (var barn : person.barn()) {
            assertThat(barn.fnr()).isNotNull();
            assertThat(barn.fødselsdato()).isNotNull();
            assertThat(barn.kjønn()).isNotNull();
            assertThat(barn.annenForelder()).isNull();
        }

    }

    public static Person personMedAnnenpart() {
        var annenpartFnr = new Fødselsnummer("33333344444");
        var annenpartAktørId = new AktørId("9999999999");
        var annenpartFødselsdato = LocalDate.now().minusYears(23);
        var annenpartFornavn = "Guro";
        var annenpartMellomnavn = "";
        var annenpartEtternavn = "Skoskaft";
        var annenPartFelles = new AnnenPart(annenpartFnr,
            annenpartAktørId,
            new Navn(annenpartFornavn, annenpartMellomnavn, annenpartEtternavn),
            annenpartFødselsdato);

        var barnFnr = new Fødselsnummer("11111122222");
        var barnFødselsdato = LocalDate.now().minusMonths(5);
        var barnFelles = new Barn(barnFnr, barnFødselsdato, null, null, Kjønn.K, annenPartFelles);

        var søkerFnr = new Fødselsnummer("12345612345");
        var søkerAktørId = new AktørId("9988888877777");
        var søkerFornavn = "Kvikk";
        var søkerEtternavn = "Flakk";
        return new Person(søkerAktørId,
            søkerFnr,
            LocalDate.now().minusYears(25),
            new Navn(søkerFornavn, null, søkerEtternavn),
            Kjønn.M,
            Målform.NB,
            Bankkonto.UKJENT,
            List.of(barnFelles),
            new Sivilstand(Sivilstand.SivilstandType.GIFT));
    }

    public static Person personUtenAnnenpart() {
        var barnFnr1 = new Fødselsnummer("11111122222");
        var barnFnr2 = new Fødselsnummer("22222233333");
        var barnFødselsdato1 = LocalDate.now().minusMonths(18);
        var barnFødselsdato2 = LocalDate.now().minusMonths(5);
        var barnFelles1 = new Barn(barnFnr1, barnFødselsdato1, null, null, Kjønn.K, null);
        var barnFelles2 = new Barn(barnFnr2, barnFødselsdato2, null, null, Kjønn.M, null);

        var søkerFnr = new Fødselsnummer("12345612345");
        var søkerAktørId = new AktørId("9988888877777");
        var søkerFornavn = "Kvikk";
        var søkerEtternavn = "Flakk";
        return new Person(søkerAktørId,
            søkerFnr,
            LocalDate.now().minusYears(30),
            new Navn(søkerFornavn, null, søkerEtternavn),
            Kjønn.M,
            Målform.NB,
            null,
            List.of(barnFelles1, barnFelles2),
            new Sivilstand(Sivilstand.SivilstandType.SEPARERT));
    }

    public static List<Arbeidsforhold> arbeidsforhold() {
        return List.of(new Arbeidsforhold(ARBEIDSGIVER_ID,
            ARBEIDSGIVER_ID_TYPE,
            ARBEIDSGIVER_NAVN,
            STILLINGSPROSENT,
            FOM_ARBEIDSFORHOLD,
            TOM_ARBEIDSFORHOLD));
    }
}
