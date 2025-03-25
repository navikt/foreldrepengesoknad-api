package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import jakarta.validation.constraints.NotNull;

public record AktivMellomlagringDto(@NotNull boolean engangsstonad,@NotNull boolean foreldrepenger,@NotNull boolean svangerskapspenger) {
}
