package no.nav.foreldrepenger.selvbetjening.innsyn.sakerV2;

import java.util.Set;

public record Saker(Set<FpSak> foreldrepenger,
             Set<EsSak> engangsstønad,
             Set<SvpSak> svangerskapspenger) {
}
