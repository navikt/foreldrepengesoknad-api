package no.nav.foreldrepenger.selvbetjening.oppslag;

import static java.time.LocalDate.now;
import static no.nav.foreldrepenger.selvbetjening.oppslag.OppslagPersonMapperTest.arbeidsforhold;
import static no.nav.foreldrepenger.selvbetjening.oppslag.OppslagPersonMapperTest.personMedAnnenpart;
import static no.nav.foreldrepenger.selvbetjening.oppslag.OppslagPersonMapperTest.personUtenAnnenpart;
import static no.nav.foreldrepenger.selvbetjening.oppslag.mapper.PersonMapper.tilPersonFrontend;
import static no.nav.foreldrepenger.selvbetjening.util.DeseraliseringSeraliseringTestUtils.testDeseraliseringProdusererSammeObjekt;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.domain.felles.Kjønn;
import no.nav.foreldrepenger.selvbetjening.config.JacksonConfiguration;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.Arbeidsforhold;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.Søkerinfo;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JacksonConfiguration.class})
class OppslagSeraliseringTest {

    private static final String ARBEIDSGIVER_ID = "123456789";
    private static final String ARBEIDSGIVER_ID_TYPE = "orgnr";
    private static final String ARBEIDSGIVER_NAVN = "KJELL T. RINGS SYKKELVERKSTED";
    private static final Double STILLINGSPROSENT = 100d;
    private static final LocalDate FOM_ARBEIDSFORHOLD = now().minusYears(2);
    @Autowired
    private ObjectMapper mapper;

    @Test
    void verifiserOppslagAvPersonIkkeEndresUnderSeraliseringOgDeseralisering() {
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

        testDeseraliseringProdusererSammeObjekt(person, mapper, true);
    }

    @Test
    void verifiserKorrektSeraliseringDeseraliseringAvPersonMedBarnUtenAnnenpart() {
        var person = tilPersonFrontend(personUtenAnnenpart());
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

        var deseralisertArbeidsforhold = mapper.readValue(arbeidsforholdSeralized, Arbeidsforhold.class);
        assertThat(deseralisertArbeidsforhold.fom()).isEqualTo(FOM_ARBEIDSFORHOLD);
    }

    @Test
    void testSøkerInfo() {
        var søkerinfo = new Søkerinfo(tilPersonFrontend(personMedAnnenpart()), arbeidsforhold());
        testDeseraliseringProdusererSammeObjekt(søkerinfo, mapper, true);
    }
}
