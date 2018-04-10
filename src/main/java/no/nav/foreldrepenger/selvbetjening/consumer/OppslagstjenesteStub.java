package no.nav.foreldrepenger.selvbetjening.consumer;

import static java.time.LocalDate.now;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.consumer.json.AdresseDto;
import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "true")
public class OppslagstjenesteStub implements Oppslag {

    private static final Logger LOG = getLogger(OppslagstjenesteStub.class);

    @Override
    public PersonDto hentPerson() {
        LOG.info("Stubber oppslag...");
        return person();
    }

    private PersonDto person() {
        PersonDto dto = new PersonDto();
        dto.fnr = "25987148243";
        dto.aktorId = "0123456789999";
        dto.fornavn = "Siv";
        dto.mellomnavn = "Bjarne";
        dto.etternavn = "Stubsveen";
        dto.fodselsdato = now().minusYears(21);
        dto.kjonn = "K";
        dto.adresse = new AdresseDto();
        return dto;
    }
}
