package no.nav.foreldrepenger.selvbetjening.uttak;

import java.util.Map;

import no.nav.foreldrepenger.regler.uttak.beregnkontoer.StønadskontoBeregningStønadskontotype;

public record KontoBeregning(Map<StønadskontoBeregningStønadskontotype, Integer> kontoer, Minsteretter minsteretter) {

}
