package no.nav.foreldrepenger.selvbetjening.uttak;

import java.util.List;


public record KontoBeregningDto(List<KontoDto> kontoer, Minsteretter minsteretter, Tillegg tillegg) {

    public record Minsteretter(int farRundtFødsel, int toTette) {
    }

    public record Tillegg(int flerbarn, int prematur) {
    }

    public record KontoDto(KontoType konto, int dager) {
        public enum KontoType {
            MØDREKVOTE,
            FEDREKVOTE,
            FELLESPERIODE,
            FORELDREPENGER,
            FORELDREPENGER_FØR_FØDSEL,
            AKTIVITETSFRI_KVOTE
        }
    }

}
