package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.svangerskapspenger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import no.nav.foreldrepenger.common.oppslag.dkif.Målform;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.AnnenInntektDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.FrilansInformasjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.NæringDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.AvtaltFerie;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.validering.VedlegglistestørrelseConstraint;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;

public record SvangerskapspengesøknadDto(LocalDate mottattdato,
                                         @NotNull Målform språkkode,
                                         @Valid @NotNull BarnDto barn,
                                         @Valid FrilansInformasjonDto frilansInformasjon,
                                         @Valid @Size(max = 15) List<@Valid @NotNull NæringDto> selvstendigNæringsdrivendeInformasjon,
                                         @Valid @Size(max = 15) List<@Valid @NotNull AnnenInntektDto> andreInntekterSiste10Mnd,
                                         @Valid @Size(max = 40) List<@Valid @NotNull UtenlandsoppholdsperiodeDto> utenlandsopphold,
                                         @Valid @NotNull @Size(max = 100) List<@Valid @NotNull TilretteleggingDto> tilretteleggingsbehov,
                                         @Valid @Size(max = 100) List<@Valid @NotNull AvtaltFerie> avtalteFerieperioder,
                                         @Valid @VedlegglistestørrelseConstraint @Size(max = 100) List<@Valid  @NotNull VedleggDto> vedlegg) implements SøknadDto {

    public SvangerskapspengesøknadDto {
        selvstendigNæringsdrivendeInformasjon = Optional.ofNullable(selvstendigNæringsdrivendeInformasjon).orElse(List.of());
        andreInntekterSiste10Mnd = Optional.ofNullable(andreInntekterSiste10Mnd).orElse(List.of());
        utenlandsopphold = Optional.ofNullable(utenlandsopphold).orElse(List.of());
        tilretteleggingsbehov = Optional.ofNullable(tilretteleggingsbehov).orElse(List.of());
        vedlegg = Optional.ofNullable(vedlegg).orElse(List.of());
        avtalteFerieperioder = Optional.ofNullable(avtalteFerieperioder).orElse(List.of());
    }
}
