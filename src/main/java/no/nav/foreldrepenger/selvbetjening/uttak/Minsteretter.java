package no.nav.foreldrepenger.selvbetjening.uttak;

import java.util.Map;

import no.nav.foreldrepenger.regler.uttak.beregnkontoer.Minsterett;

public record Minsteretter(int generellMinsterett, int farRundtFødsel) {

    static Minsteretter from(Map<Minsterett, Integer> map) {
        //Slår sammen generell og uten_akt
        return new Minsteretter(
            Math.max(map.getOrDefault(Minsterett.GENERELL_MINSTERETT, 0), map.getOrDefault(Minsterett.UTEN_AKTIVITETSKRAV, 0)),
            map.getOrDefault(Minsterett.FAR_UTTAK_RUNDT_FØDSEL, 0)
        );
    }
}
