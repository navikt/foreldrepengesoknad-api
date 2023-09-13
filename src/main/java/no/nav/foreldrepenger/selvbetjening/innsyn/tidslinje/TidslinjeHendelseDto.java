package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import no.nav.foreldrepenger.common.innsyn.Arbeidsgiver;

public record TidslinjeHendelseDto(LocalDateTime opprettet,
                                   String journalpostId,
                                   AktørType aktørType,
                                   TidslinjeHendelseType tidslinjeHendelseType,
                                   Arbeidsgiver arbeidsgiver, // Brukes ikke
                                   List<Dokument> dokumenter) { // Brukes ikke

    enum AktørType {
        BRUKER,
        NAV,
        ARBEIDSGIVER
    }

    enum TidslinjeHendelseType {
        FØRSTEGANGSSØKNAD,
        FØRSTEGANGSSØKNAD_NY,
        ETTERSENDING,
        ENDRINGSSØKNAD,
        INNTEKTSMELDING,
        VEDTAK,
        VEDTAK_FØRSTEGANG,
        VEDTAK_ENDRING,
        VEDTAK_TILBAKEKREVING,
        VENTER_INNTEKTSMELDING,
        UTGÅENDE_INNHENT_OPPLYSNINGER,
        UTGÅENDE_ETTERLYS_INNTEKTSMELDING
    }

    public record Dokument(String dokumentId, String dokumentTypeId) {

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Dokument dokument = (Dokument) o;
            return Objects.equals(dokumentId, dokument.dokumentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dokumentId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TidslinjeHendelseDto that = (TidslinjeHendelseDto) o;
        return aktørType == that.aktørType &&
            tidslinjeHendelseType == that.tidslinjeHendelseType &&
            Objects.equals(arbeidsgiver, that.arbeidsgiver) &&
            Objects.equals(dokumenter, that.dokumenter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aktørType, tidslinjeHendelseType, arbeidsgiver, dokumenter);
    }
}
