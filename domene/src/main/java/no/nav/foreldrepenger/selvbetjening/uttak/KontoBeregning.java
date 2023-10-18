package no.nav.foreldrepenger.selvbetjening.uttak;

import java.util.Map;

import no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoBeregningStønadskontotype;


public record KontoBeregning(Map<StønadskontoBeregningStønadskontotype, Integer> kontoer, Minsteretter minsteretter) {

}
