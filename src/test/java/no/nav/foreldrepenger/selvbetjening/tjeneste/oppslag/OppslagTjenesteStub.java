package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static com.neovisionaries.i18n.CountryCode.NO;
import static java.time.LocalDate.now;
import static java.util.Collections.singletonList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AktørId;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.AnnenForelder;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Arbeidsforhold;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Bankkonto;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Barn;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Person;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Søkerinfo;
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.PersonDto;

@Service("oppslagTjeneste")
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "true")
public class OppslagTjenesteStub implements Oppslag {
    private static final Logger LOG = getLogger(OppslagTjenesteStub.class);

    @Override
    public String ping() {
        return "hello earthlings";
    }

    @Override
    public Person hentPerson() {
        LOG.info("Stubber oppslag...");
        return person();
    }

    @Override
    public Søkerinfo hentSøkerinfo() {
        return new Søkerinfo(person(), arbeidsforhold());
    }

    @Override
    public AktørId hentAktørId(String fnr) {
        return new AktørId("1234567890");
    }

    private static Person person() {
        PersonDto dto = personDto();
        return new Person(dto);
    }

    public static PersonDto personDto() {
        PersonDto dto = new PersonDto();
        dto.fnr = "25987148243";
        dto.aktorId = "0123456789999";
        dto.fornavn = "SIGRID";
        dto.etternavn = "HOELSVEEN";
        dto.fødselsdato = now().minusYears(21);
        dto.kjønn = "K";
        dto.landKode = NO;
        dto.bankkonto = new Bankkonto("1234567890", "Stub NOR");
        dto.barn = barn();
        return dto;
    }

    private static AnnenForelder annenForelder() {
        return new AnnenForelder("01017098765", "Steve", "Grønland", "Nichols",
                now().minusYears(45));
    }

    private static List<Barn> barn() {
        return singletonList(
                new Barn("01011812345", "Mo", null, "Hoelsveen", "M", now().minusYears(1), annenForelder()));
    }

    private static List<Arbeidsforhold> arbeidsforhold() {
        return singletonList(new Arbeidsforhold("123456789", "orgnr", "KJELL T. RINGS SYKKELVERKSTED", 100d,
                now().minusYears(2), null));
    }

}
