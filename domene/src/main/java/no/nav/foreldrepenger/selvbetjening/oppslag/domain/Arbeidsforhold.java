package no.nav.foreldrepenger.selvbetjening.oppslag.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public record Arbeidsforhold(@NotNull String arbeidsgiverId,
                             @NotNull String arbeidsgiverIdType,
                             @NotNull String arbeidsgiverNavn,
                             @NotNull Double stillingsprosent,
                             @NotNull @JsonAlias("from") LocalDate fom,
                             @JsonAlias("to") LocalDate tom) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Arbeidsforhold that = (Arbeidsforhold) o;
        return Objects.equals(fom, that.fom) &&
                Objects.equals(tom, that.tom) &&
                Objects.equals(arbeidsgiverId, that.arbeidsgiverId) &&
                Objects.equals(arbeidsgiverNavn, that.arbeidsgiverNavn) &&
                sammenlignProsentRelativTilFomOgTomSomMottakIkkeHensyntar(that) &&
                Objects.equals(arbeidsgiverIdType, that.arbeidsgiverIdType);
    }

    private boolean sammenlignProsentRelativTilFomOgTomSomMottakIkkeHensyntar(Arbeidsforhold that) {
        if (tom != null && LocalDate.now().isAfter(tom)) {
            return true; // Feil i mottak hvor perioden ikke hensyntas
        }

        return Objects.equals(stillingsprosent, that.stillingsprosent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arbeidsgiverId, arbeidsgiverIdType, arbeidsgiverNavn, stillingsprosent, fom, tom);
    }
}
