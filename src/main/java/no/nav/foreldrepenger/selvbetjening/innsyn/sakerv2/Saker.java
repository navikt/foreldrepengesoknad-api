package no.nav.foreldrepenger.selvbetjening.innsyn.sakerv2;

import java.util.Set;

public record Saker(Set<FpSak> foreldrepenger,
             Set<EsSak> engangsstønad,
             Set<SvpSak> svangerskapspenger) {
}
