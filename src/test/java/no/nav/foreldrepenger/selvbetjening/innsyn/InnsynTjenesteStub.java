package no.nav.foreldrepenger.selvbetjening.innsyn;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Behandling;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;
import no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2.Saker;
import no.nav.foreldrepenger.selvbetjening.innsyn.uttaksplan.Uttaksplan;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "true")
public class InnsynTjenesteStub implements Innsyn {

    @Override
    public String ping() {
        return "hello earthlings";
    }

    @Override
    public Uttaksplan hentUttaksplan(String saksnummer) {
        return null;
    }

    @Override
    public List<Sak> hentSaker() {
        LocalDateTime mottattdato = LocalDateTime.now().minusDays(7);

        Behandling behandling = new Behandling(mottattdato, mottattdato.plusHours(2), "AVSLU", "FP",
                "FORP_FODS", null, "4869", "NAV Torrevieja", Collections.emptyList());

        return Arrays.asList(
                new Sak("SAK", "123234545", "UBEH", now().minusYears(1), "LA8PV", null, "SVP", emptyList(), false),
                new Sak("FPSAK", "424242424", "LOP", mottattdato.toLocalDate(), null, null, "SVP",
                        singletonList(behandling), true));
    }

    @Override
    public Uttaksplan hentUttaksplanAnnenPart(String annenPart) {
        return null;
    }

    @Override
    public Saker hentSakerV2() {
        return new Saker(Set.of(), Set.of(), Set.of());
    }

}
