package no.nav.foreldrepenger.selvbetjening.oppslag;

import static no.nav.foreldrepenger.selvbetjening.oppslag.OppslagSeraliseringTest.arbeidsforhold;
import static no.nav.foreldrepenger.selvbetjening.oppslag.mapper.PersonMapper.tilPersonFrontend;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.oppslag.domain.PersonFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Søkerinfo;

@Service("oppslagTjeneste")
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "true")
public class OppslagTjenesteStub implements Oppslag {
    private static final Logger LOG = getLogger(OppslagTjenesteStub.class);

    @Override
    public String ping() {
        return "hello earthlings";
    }

    @Override
    public Søkerinfo hentSøkerinfo() {
        return new Søkerinfo(person(), arbeidsforhold());
    }

    private static PersonFrontend person() {
        return tilPersonFrontend(OppslagSeraliseringTest.person());
    }
}
