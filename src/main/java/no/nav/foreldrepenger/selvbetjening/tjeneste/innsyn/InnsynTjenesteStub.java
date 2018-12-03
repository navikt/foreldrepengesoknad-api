package no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Behandling;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.BehandlingResultatType;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.tjeneste.innsyn.uttaksplan.UttaksPeriode;

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
        Behandling behandling = new Behandling(LocalDateTime.now(), LocalDateTime.now(), "UTRED", "FP",
                "FORP_FODS", null, BehandlingResultatType.AVSLÅTT, "4833", "NAV Torrevieja");

        return Arrays.asList(
                new Sak("SAK", "sak123", "UBEH", now(), emptyList()),
                new Sak("FPSAK", "42", "LOP", now(), singletonList(behandling)));
    }
}
