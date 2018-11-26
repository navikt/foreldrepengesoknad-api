package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Behandling;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "true")
public class InnsynTjenesteStub implements Innsyn {

    @Override
    public String ping() {
        return "hello earthlings";
    }

    @Override
    public URI pingURI() {
        return URI.create("http.//www.vg.no");
    }

    @Override
    public List<UttaksPeriode> hentUttaksplan(String saksnummer) {
        return null;
    }

    @Override
    public List<Sak> hentSaker() {
        Behandling behandling = new Behandling("abc", "UTRED", "FP", "FORP_FODS", null, "4833", "NAV Torrevieja");
        return Arrays.asList(
                new Sak("sak123", "UBEH", now(), emptyList()),
                new Sak("42", "LOP", now(), singletonList(behandling)));
    }
}
