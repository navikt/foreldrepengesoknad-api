package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype.BARE_FAR_RETT;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype.FAR_RUNDT_FØDSEL;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype.TETTE_SAKER_FAR;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype.TETTE_SAKER_MOR;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype.UFØREDAGER;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Brukerrolle;

@Deprecated
public record KontoBeregning(Map<StønadskontoBeregningStønadskontotype, Integer> kontoer, Minsteretter minsteretter) {

    public static KontoBeregning fra(Map<StønadskontoKontotype, Integer> kontoer, Brukerrolle brukerrolle) {
        return new KontoBeregning(tilStønadsKontoer(kontoer), tilMinsteretter(kontoer, brukerrolle));
    }

    private static Map<StønadskontoBeregningStønadskontotype, Integer> tilStønadsKontoer(Map<StønadskontoKontotype, Integer> kontoer) {
        var temp = new EnumMap<StønadskontoBeregningStønadskontotype, Integer>(StønadskontoBeregningStønadskontotype.class);
        kontoer.forEach((konto, verdi) -> tilKontoBeregning(konto).ifPresent(kontoType -> temp.put(kontoType, verdi)));
        return temp;
    }

    private static Minsteretter tilMinsteretter(Map<StønadskontoKontotype, Integer> kontoer, Brukerrolle brukerrolle) {
        var generellMinsterett = Math.max(
            kontoer.getOrDefault(BARE_FAR_RETT, 0),
            kontoer.getOrDefault(UFØREDAGER, 0)
        );

        var farRundtFødsel = kontoer.getOrDefault(FAR_RUNDT_FØDSEL, 0);
        var toTette = toTetteFra(kontoer, brukerrolle);
        return new Minsteretter(generellMinsterett, farRundtFødsel, toTette);
    }

    private static Integer toTetteFra(Map<StønadskontoKontotype, Integer> kontoer, Brukerrolle brukerrolle) {
        if (brukerrolle.equals(Brukerrolle.MOR)) {
            return kontoer.getOrDefault(TETTE_SAKER_MOR, 0);
        } else {
            return kontoer.getOrDefault(TETTE_SAKER_FAR, 0);
        }
    }

    private static Optional<StønadskontoBeregningStønadskontotype> tilKontoBeregning(StønadskontoKontotype konto) {
        return switch (konto) {
            case FELLESPERIODE -> Optional.of(StønadskontoBeregningStønadskontotype.FELLESPERIODE);
            case MØDREKVOTE -> Optional.of(StønadskontoBeregningStønadskontotype.MØDREKVOTE);
            case FEDREKVOTE -> Optional.of(StønadskontoBeregningStønadskontotype.FEDREKVOTE);
            case FORELDREPENGER -> Optional.of(StønadskontoBeregningStønadskontotype.FORELDREPENGER);
            case FORELDREPENGER_FØR_FØDSEL -> Optional.of(StønadskontoBeregningStønadskontotype.FORELDREPENGER_FØR_FØDSEL);
            case FLERBARNSDAGER -> Optional.of(StønadskontoBeregningStønadskontotype.FLERBARNSDAGER);
            default -> Optional.empty();
        };
    }


    public enum StønadskontoBeregningStønadskontotype {
        MØDREKVOTE,
        FEDREKVOTE,
        FELLESPERIODE,
        FORELDREPENGER,
        FORELDREPENGER_FØR_FØDSEL,
        FLERBARNSDAGER
    }

    public record Minsteretter(int generellMinsterett, int farRundtFødsel, int toTette) {
    }
}
