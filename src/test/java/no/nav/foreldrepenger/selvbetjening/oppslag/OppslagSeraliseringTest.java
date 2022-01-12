package no.nav.foreldrepenger.selvbetjening.oppslag;

import static java.time.LocalDate.now;
import static no.nav.foreldrepenger.selvbetjening.oppslag.mapper.PersonMapper.tilPersonFrontend;
import static no.nav.foreldrepenger.selvbetjening.util.DeseraliseringSeraliseringTestUtils.testDeseraliseringProdusererSammeObjekt;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.i18n.CountryCode;

import no.nav.foreldrepenger.common.domain.AktørId;
import no.nav.foreldrepenger.common.domain.Barn;
import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Navn;
import no.nav.foreldrepenger.common.domain.felles.AnnenPart;
import no.nav.foreldrepenger.common.domain.felles.Bankkonto;
import no.nav.foreldrepenger.common.domain.felles.Kjønn;
import no.nav.foreldrepenger.common.domain.felles.Person;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Arbeidsforhold;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JacksonConfiguration.class})
class OppslagSeraliseringTest {

    private static final String ARBEIDSGIVER_ID = "123456789";
    private static final String ARBEIDSGIVER_ID_TYPE = "orgnr";
    private static final String ARBEIDSGIVER_NAVN = "KJELL T. RINGS SYKKELVERKSTED";
    private static final Double STILLINGSPROSENT = 100d;
    private static final LocalDate FOM_ARBEIDSFORHOLD = now().minusYears(2);
    private static final LocalDate TOM_ARBEIDSFORHOLD = null;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void verifiserOppslagAvPersonIkkeEndresUnderSeraliseringOgDeseralisering() {
        var personFelles = person();
        var barnFelles = personFelles.barn().iterator().next();
        var annenPartFelles = barnFelles.annenPart();

        var person = tilPersonFrontend(personFelles);
        assertThat(person.fnr()).isEqualTo(personFelles.fnr().getFnr());
        assertThat(person.fornavn()).isEqualTo(personFelles.navn().fornavn());
        assertThat(person.etternavn()).isEqualTo(personFelles.navn().etternavn());
        assertThat(person.kjønn()).isEqualTo(personFelles.kjønn().name());
        assertThat(person.ikkeNordiskEøsLand()).isFalse();
        assertThat(person.barn()).hasSize(1);
        var barn = person.barn().get(0);

        assertThat(barn.fnr()).isEqualTo(barnFelles.fnr().getFnr());
        assertThat(barn.fødselsdato()).isEqualTo(barnFelles.fødselsdato());
        assertThat(barn.kjønn()).isEqualTo(Kjønn.K.name());
        var annenforelder = barn.annenForelder();
        assertThat(annenforelder).isNotNull();
        assertThat(annenforelder.fnr()).isEqualTo(annenPartFelles.fnr().getFnr());
        assertThat(annenforelder.fødselsdato()).isEqualTo(annenPartFelles.fødselsdato());
        assertThat(annenforelder.fornavn()).isEqualTo(annenPartFelles.navn().fornavn());
        assertThat(annenforelder.mellomnavn()).isEqualTo(annenPartFelles.navn().mellomnavn());
        assertThat(annenforelder.etternavn()).isEqualTo(annenPartFelles.navn().etternavn());

        testDeseraliseringProdusererSammeObjekt(person, mapper, true);
    }

    @Test
    void testArbeidsforholdDeseraliseringMedAlias() throws JsonProcessingException {

        var arbeidsforholdSeralized = String.format("""
            {
                "arbeidsgiverId" : "%s",
                "arbeidsgiverIdType" : "%s",
                "arbeidsgiverNavn" : "%s",
                "stillingsprosent" : %d,
                "from" : "%s"
            }
            """,
            ARBEIDSGIVER_ID, ARBEIDSGIVER_ID_TYPE, ARBEIDSGIVER_NAVN, STILLINGSPROSENT.intValue(), FOM_ARBEIDSFORHOLD);

        var deserialized = mapper.readValue(arbeidsforholdSeralized, Arbeidsforhold.class);
        assertThat(deserialized.fom()).isEqualTo(FOM_ARBEIDSFORHOLD);
    }

    @Test
    void testSøkerInfo() {
        var søkerinfo = new Søkerinfo(tilPersonFrontend(person()), arbeidsforhold());
        testDeseraliseringProdusererSammeObjekt(søkerinfo, mapper, true);
    }


    public static Person person() {
        var annenpartFnr = Fødselsnummer.valueOf("33333344444");
        var annenpartAktørId = AktørId.valueOf("9999999999");
        var annenpartFødselsdato = LocalDate.now().minusYears(23);
        var annenpartFornavn = "Guro";
        var annenpartMellomnavn = "";
        var annenpartEtternavn = "Skoskaft";
        var annenPartFelles = new AnnenPart(annenpartFnr, annenpartAktørId,
            new Navn(annenpartFornavn, annenpartMellomnavn, annenpartEtternavn), annenpartFødselsdato);

        var barnFnr = new Fødselsnummer("11111122222");
        var barnFødselsdato = LocalDate.now().minusMonths(5);
        var barnFelles = new Barn(barnFnr, barnFødselsdato, null, Kjønn.K, annenPartFelles);

        var søkerFnr = Fødselsnummer.valueOf("12345612345");
        var søkerAktørId = AktørId.valueOf("9988888877777");
        var søkerFornavn = "Kvikk";
        var søkerEtternavn = "Flakk";
        var personFelles = new Person(søkerAktørId, søkerFnr, LocalDate.now().minusYears(25),
            new Navn(søkerFornavn, null,  søkerEtternavn), Kjønn.M, Målform.NB, CountryCode.NO,
            Bankkonto.UKJENT, Set.of(barnFelles));
        return personFelles;
    }

    public static List<Arbeidsforhold> arbeidsforhold() {
        return List.of(new Arbeidsforhold(ARBEIDSGIVER_ID, ARBEIDSGIVER_ID_TYPE, ARBEIDSGIVER_NAVN, STILLINGSPROSENT,
            FOM_ARBEIDSFORHOLD, TOM_ARBEIDSFORHOLD));
    }
}
