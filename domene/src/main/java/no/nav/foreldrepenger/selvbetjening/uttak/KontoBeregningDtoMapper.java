package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype.BARE_FAR_RETT;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype.FAR_RUNDT_FØDSEL;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype.TETTE_SAKER_FAR;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype.TETTE_SAKER_MOR;
import static no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype.UFØREDAGER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import no.nav.foreldrepenger.stønadskonto.regelmodell.StønadskontoKontotype;
import no.nav.foreldrepenger.stønadskonto.regelmodell.grunnlag.Brukerrolle;

public class KontoBeregningDtoMapper {

    private KontoBeregningDtoMapper() {
        // Statisk implementasjon
    }

    public static KontoBeregningDto tilKontoberegning(Map<StønadskontoKontotype, Integer> stønadskontoer, Brukerrolle brukerrolle) {
        return new KontoBeregningDto(tilKontoer(stønadskontoer), tilMinsteretter(stønadskontoer, brukerrolle));
    }

    private static List<KontoBeregningDto.KontoDto> tilKontoer(Map<StønadskontoKontotype, Integer> stønadskontoer) {
        var kontoer = new ArrayList<KontoBeregningDto.KontoDto>();
        var generellMinsterett = generellMinsterettFra(stønadskontoer);
        if (generellMinsterett > 0) {
            kontoer.add(new KontoBeregningDto.KontoDto(KontoBeregningDto.KontoDto.KontoType.AKTIVITETSFRI_KVOTE, generellMinsterett));
        }
        stønadskontoer.entrySet()
            .stream()
            .filter(k -> tilKontoType(k.getKey()).isPresent())
            .map(k -> getKontoDto(k, generellMinsterett))
            .forEach(kontoer::add);
        return kontoer;
    }

    private static KontoBeregningDto.KontoDto getKontoDto(Map.Entry<StønadskontoKontotype, Integer> stønadskonto, int generellMinsterett) {
        var kontoType = tilKontoType(stønadskonto.getKey()).orElseThrow();
        if (generellMinsterett > 0 && kontoType.equals(KontoBeregningDto.KontoDto.KontoType.FORELDREPENGER)) {
            return new KontoBeregningDto.KontoDto(kontoType, stønadskonto.getValue() - generellMinsterett);
        }
        return new KontoBeregningDto.KontoDto(kontoType, stønadskonto.getValue());
    }

    private static KontoBeregningDto.Minsteretter tilMinsteretter(Map<StønadskontoKontotype, Integer> kontoer, Brukerrolle brukerrolle) {
        return new KontoBeregningDto.Minsteretter(kontoer.getOrDefault(FAR_RUNDT_FØDSEL, 0), toTetteFra(kontoer, brukerrolle));
    }

    private static int generellMinsterettFra(Map<StønadskontoKontotype, Integer> kontoer) {
        return Math.max(
            kontoer.getOrDefault(BARE_FAR_RETT, 0), // Etter WLB
            kontoer.getOrDefault(UFØREDAGER, 0) // Før WLB
        );
    }

    private static Integer toTetteFra(Map<StønadskontoKontotype, Integer> kontoer, Brukerrolle brukerrolle) {
        if (brukerrolle.equals(Brukerrolle.MOR)) {
            return kontoer.getOrDefault(TETTE_SAKER_MOR, 0);
        } else {
            return kontoer.getOrDefault(TETTE_SAKER_FAR, 0);
        }
    }

    private static Optional<KontoBeregningDto.KontoDto.KontoType> tilKontoType(StønadskontoKontotype konto) {
        return switch (konto) {
            case FELLESPERIODE -> Optional.of(KontoBeregningDto.KontoDto.KontoType.FELLESPERIODE);
            case MØDREKVOTE -> Optional.of(KontoBeregningDto.KontoDto.KontoType.MØDREKVOTE);
            case FEDREKVOTE -> Optional.of(KontoBeregningDto.KontoDto.KontoType.FEDREKVOTE);
            case FORELDREPENGER -> Optional.of(KontoBeregningDto.KontoDto.KontoType.FORELDREPENGER);
            case FORELDREPENGER_FØR_FØDSEL -> Optional.of(KontoBeregningDto.KontoDto.KontoType.FORELDREPENGER_FØR_FØDSEL);
            default -> Optional.empty();
        };
    }
}
