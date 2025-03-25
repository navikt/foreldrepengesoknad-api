package no.nav.foreldrepenger.selvbetjening.uttak;

import jakarta.validation.constraints.NotNull;

import java.util.List;


public record KontoBeregningDto(@NotNull List<KontoDto> kontoer, @NotNull Minsteretter minsteretter, Tillegg tillegg) {

    public record Minsteretter(@NotNull int farRundtFødsel, @NotNull int toTette) {
    }

    public record Tillegg(@NotNull int flerbarn, @NotNull int prematur) {
    }

    public record KontoDto(@NotNull KontoType konto, @NotNull int dager) {
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
