package no.nav.foreldrepenger.selvbetjening.innsyn;

import static java.time.LocalDate.now;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.Saksnummer;
import no.nav.foreldrepenger.common.innsyn.uttaksplan.UttaksplanDto;
import no.nav.foreldrepenger.common.innsyn.v2.Saker;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Behandling;
import no.nav.foreldrepenger.selvbetjening.innsyn.saker.Sak;

@Service
@ConditionalOnProperty(name = "stub.oppslag", havingValue = "true")
public class InnsynTjenesteStub implements Innsyn {

    @Override
    public String ping() {
        return "hello earthlings";
    }

    @Override
    public UttaksplanDto hentUttaksplan(Saksnummer saksnummer) {
        return null;
    }

    @Override
    public List<Sak> hentSaker() {
        var mottattdato = LocalDateTime.now().minusDays(7);

        var behandling = new Behandling(mottattdato, mottattdato.plusHours(2), "AVSLU", "FP",
                "FORP_FODS", null, "4869", "NAV Torrevieja", List.of());

        return List.of(
                new Sak("123234545", "UBEH", now().minusYears(1), "LA8PV", null, "SVP", List.of(), false),
                new Sak("424242424", "LOP", mottattdato.toLocalDate(), null, null, "SVP", List.of(behandling), true));
    }

    @Override
    public UttaksplanDto hentUttaksplanAnnenPart(Fødselsnummer annenPart) {
        return null;
    }

    @Override
    public Saker hentSakerV2() {
        return new Saker(Set.of(), Set.of(), Set.of());
    }

}
