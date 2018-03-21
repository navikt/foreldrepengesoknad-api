package no.nav.foreldrepenger.selvbetjening.consumer;

import no.nav.foreldrepenger.selvbetjening.consumer.json.AdresseDto;
import no.nav.foreldrepenger.selvbetjening.consumer.json.PersonDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.net.URI;

import static java.time.LocalDate.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Service
public class Oppslagstjeneste {

    private static final Logger LOG = getLogger(Oppslagstjeneste.class);

    private final RestTemplate template;
    private final URI oppslagServiceUrl;

    @Value("${stub.oppslag:false}")
    private boolean stub;

    @Inject
    public Oppslagstjeneste(@Value("${FPSOKNAD_OPPSLAG_API_URL}") URI uri, RestTemplate template) {
        this.oppslagServiceUrl = uri;
        this.template = template;
    }

    public PersonDto hentPerson() {
        if (stub) {
            LOG.info("Stubber oppslag...");
            return person();
        }
        URI url = fromUri(oppslagServiceUrl).path("/person").build().toUri();
        LOG.info("Oppslag URL: {}", url);

        return template.getForObject(url, PersonDto.class);
    }

    private PersonDto person() {
        PersonDto dto = new PersonDto();
        dto.fnr = "25987148243";
        dto.aktorId = "0123456789999";
        dto.fornavn = "Siv";
        dto.etternavn = "Stubsveen";
        dto.fodselsdato = now().minusYears(21);
        dto.kjonn = "K";
        dto.adresse = new AdresseDto();
        return dto;
    }
}
