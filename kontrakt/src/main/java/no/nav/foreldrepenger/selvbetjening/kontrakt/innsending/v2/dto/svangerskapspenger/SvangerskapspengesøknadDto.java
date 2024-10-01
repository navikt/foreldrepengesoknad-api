package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering.VedlegglistestørrelseConstraint;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FrilansDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger.tilrettelegging.TilretteleggingDto;

public record SvangerskapspengesøknadDto(LocalDate mottattdato,
                                         @Valid @NotNull BarnSvpDto barn,
                                         @Valid BrukerRolle rolle,
                                         @Valid Målform språkkode,
                                         @Valid FrilansDto frilans,
                                         @Valid NæringDto egenNæring,
                                         @Valid @Size(max = 40) List<AnnenInntektDto.@NotNull @Valid Utlandet> andreInntekterSiste10Mnd,
                                         @Valid @Size(max = 40) List<@Valid @NotNull UtenlandsoppholdsperiodeDto> utenlandsopphold,
                                         @Valid @NotNull @Size(max = 100) List<@Valid @NotNull TilretteleggingDto> tilrettelegging,
                                         @Valid @Size(max = 100) List<@Valid @NotNull AvtaltFerieDto> avtaltFerie,
                                         @Valid @VedlegglistestørrelseConstraint @Size(max = 100) List<@Valid  @NotNull VedleggDto> vedlegg) implements SøknadDto {

    public SvangerskapspengesøknadDto {
        utenlandsopphold = Optional.ofNullable(utenlandsopphold).orElse(List.of());
        tilrettelegging = Optional.ofNullable(tilrettelegging).orElse(List.of());
        andreInntekterSiste10Mnd = Optional.ofNullable(andreInntekterSiste10Mnd).orElse(List.of());
        vedlegg = Optional.ofNullable(vedlegg).orElse(List.of());
        avtaltFerie = Optional.ofNullable(avtaltFerie).orElse(List.of());
    }
}
