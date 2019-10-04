package no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag;

import static com.neovisionaries.i18n.CountryCode.NO;
import static java.time.LocalDate.now;
import static java.util.Collections.singletonList;
import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;
import java.util.ArrayList;

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
import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.dto.SøkerinfoDto;

@Service("oppslagTjeneste")
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "true")
public class OppslagTjenesteStub implements Oppslag {

    @Override
    public String ping() {
        return "hello earthlings";
    }

    @Override
    public URI pingURI() {
        return URI.create("http.//www.vg.no");
    }

    private static final Logger LOG = getLogger(OppslagTjenesteStub.class);

    @Override
    public Person hentPerson() {
        LOG.info("Stubber oppslag...");
        return new Person(person());
    }

    @Override
    public Søkerinfo hentSøkerinfo() {
        SøkerinfoDto dto = new SøkerinfoDto();
        dto.person = person();
        dto.arbeidsforhold = new ArrayList<>();
        Arbeidsforhold arbeidsforhold = new Arbeidsforhold();
        arbeidsforhold.arbeidsgiverId = "123456789";
        arbeidsforhold.arbeidsgiverIdType = "orgnr";
        arbeidsforhold.fom = now().minusYears(2);
        arbeidsforhold.arbeidsgiverNavn = "KJELL T. RINGS SYKKELVERKSTED";
        arbeidsforhold.stillingsprosent = 100d;
        dto.arbeidsforhold.add(arbeidsforhold);
        return new Søkerinfo(dto);
    }

    public static PersonDto person() {
        PersonDto dto = new PersonDto();
        dto.fnr = "25987148243";
        dto.aktorId = "0123456789999";
        dto.fornavn = "SIGRID";
        dto.etternavn = "HOELSVEEN";
        dto.fødselsdato = now().minusYears(21);
        dto.kjønn = "K";
        dto.landKode = NO;
        dto.bankkonto = new Bankkonto();
        dto.bankkonto.kontonummer = "1234567890";
        dto.bankkonto.banknavn = "Stub NOR";

        AnnenForelder annenForelder = new AnnenForelder("01017098765", "Steve", "Grønland", "Nichols",
                now().minusYears(45));

        Barn barn = new Barn("01011812345", "Mo", null, "Hoelsveen", "M", now().minusYears(1), annenForelder);
        dto.barn = singletonList(barn);

        return dto;
    }

    @Override
    public AktørId hentAktørId(String fnr) {
        return new AktørId("1234567890");
    }
}
