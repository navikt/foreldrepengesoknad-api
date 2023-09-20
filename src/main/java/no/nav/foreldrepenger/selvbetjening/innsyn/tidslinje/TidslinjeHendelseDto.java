package no.nav.foreldrepenger.selvbetjening.innsyn.tidslinje;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record TidslinjeHendelseDto(LocalDateTime opprettet,
                                   AktørType aktørType,
                                   TidslinjeHendelseType tidslinjeHendelseType,
                                   List<Dokument> dokumenter) {

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

    public record Dokument(String journalpostId, String dokumentId, String tittel, @Deprecated URI url) {
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Dokument dokument = (Dokument) o;
            return Objects.equals(dokumentId, dokument.dokumentId) && Objects.equals(tittel, dokument.tittel);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dokumentId, tittel);
        }

        @Override
        public String toString() {
            return "Dokument{" + "dokumentId='" + dokumentId + '\'' + ", tittel='" + tittel + '\'' + '}';
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
            Objects.equals(dokumenter, that.dokumenter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aktørType, tidslinjeHendelseType, dokumenter);
    }
}
