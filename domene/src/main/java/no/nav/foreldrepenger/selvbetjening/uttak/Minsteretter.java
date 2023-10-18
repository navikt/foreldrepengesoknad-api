package no.nav.foreldrepenger.selvbetjening.uttak;

import java.util.Map;

import no.nav.foreldrepenger.stønadskonto.regelmodell.Minsterett;


public record Minsteretter(int generellMinsterett, int farRundtFødsel, int toTette) {

    static Minsteretter from(Map<Minsterett, Integer> map) {
        //Slår sammen generell og uten_akt
        return new Minsteretter(
            Math.max(map.getOrDefault(Minsterett.GENERELL_MINSTERETT, 0), map.getOrDefault(Minsterett.UTEN_AKTIVITETSKRAV, 0)),
            map.getOrDefault(Minsterett.FAR_UTTAK_RUNDT_FØDSEL, 0), map.getOrDefault(Minsterett.TETTE_FØDSLER, 0)
        );
    }
}
