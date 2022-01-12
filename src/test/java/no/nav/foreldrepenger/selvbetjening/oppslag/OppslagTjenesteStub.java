package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.foreldrepenger.selvbetjening.oppslag.OppslagPersonMapperTest.arbeidsforhold;
import static no.nav.foreldrepenger.selvbetjening.oppslag.OppslagPersonMapperTest.personMedAnnenpart;
import static no.nav.foreldrepenger.selvbetjening.oppslag.mapper.PersonMapper.tilPersonFrontend;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.oppslag.domain.PersonFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;

@Service("oppslagTjeneste")
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "true")
public class OppslagTjenesteStub implements Oppslag {

    @Override
    public String ping() {
        return "hello earthlings";
    }

    @Override
    public Søkerinfo hentSøkerinfo() {
        return new Søkerinfo(person(), arbeidsforhold());
    }

    private static PersonFrontend person() {
        return tilPersonFrontend(personMedAnnenpart());
    }
}
