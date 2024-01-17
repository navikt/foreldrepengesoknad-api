package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;

import java.util.List;
import java.util.stream.Stream;

import no.nav.foreldrepenger.common.domain.felles.LukketPeriode;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.Utenlandsopphold;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Adopsjon;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.FremtidigFødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Fødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Omsorgsovertakelse;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.RelasjonTilBarn;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AdopsjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FødselDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.OmsorgsovertakelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.TerminDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;

public final class CommonMapper {

    private CommonMapper() {
    }

    static Utenlandsopphold tilOppholdIUtlandet(SøknadDto s) {
        if (s.utenlandsopphold() != null) {
            var opphold = Stream.concat(
                safeStream(s.utenlandsopphold().utenlandsoppholdSiste12Mnd()),
                safeStream(s.utenlandsopphold().utenlandsoppholdNeste12Mnd())
            ).toList();
            return new Utenlandsopphold(tilUtenlandsoppholdsliste(opphold));
        } else {
            return new Utenlandsopphold(tilUtenlandsoppholds(s.oppholdIUtlandet()));
        }
    }

    private static List<Utenlandsopphold.Opphold> tilUtenlandsoppholds(List<UtenlandsoppholdsperiodeDto> perioder) {
        return safeStream(perioder)
            .map(CommonMapper::tilUtenlandsopphold)
            .toList();
    }

    private static Utenlandsopphold.Opphold tilUtenlandsopphold(UtenlandsoppholdsperiodeDto oppholdsperiode) {
        return new Utenlandsopphold.Opphold(oppholdsperiode.landkode(), new LukketPeriode(oppholdsperiode.fom(), oppholdsperiode.tom()));
    }

    private static List<Utenlandsopphold.Opphold> tilUtenlandsoppholdsliste(List<UtenlandsoppholdDto.Periode> opphold) {
        return safeStream(opphold)
            .map(CommonMapper::tilUtenlandsopphold)
            .toList();
    }

    private static Utenlandsopphold.Opphold tilUtenlandsopphold(UtenlandsoppholdDto.Periode o) {
        return new Utenlandsopphold.Opphold(o.landkode(), new LukketPeriode(o.fom(), o.tom()));
    }

    static RelasjonTilBarn tilRelasjonTilBarn(BarnDto barn) {
        if (barn instanceof FødselDto f) {
            return tilFødsel(f);
        } else if (barn instanceof TerminDto t) {
            return tilFremtidigFødsel(t);
        } else if (barn instanceof AdopsjonDto a) {
            return tilAdopsjon(a);
        } else if (barn instanceof OmsorgsovertakelseDto o) {
            return tilOmsorgsovertagelse(o);
        } else {
            throw new IllegalStateException("Utviklerfeil: Skal ikke være mulig med annen type barn enn fødsel, termin, adopsjon eller omsorgsovertakelse");
        }
    }

    private static Fødsel tilFødsel(FødselDto barn) {
        return new Fødsel(
            barn.antallBarn(),
            List.of(barn.fødselsdato()), // TODO: Fjern liste i mottak!
            barn.termindato(),
            tilVedleggsreferanse(barn.vedleggreferanser())
        );
    }

    private static FremtidigFødsel tilFremtidigFødsel(TerminDto barn) {
        return new FremtidigFødsel(
            barn.antallBarn(),
            barn.termindato(),
            barn.terminbekreftelseDato(),
            tilVedleggsreferanse(barn.vedleggreferanser())
        );
    }

    private static Omsorgsovertakelse tilOmsorgsovertagelse(OmsorgsovertakelseDto barn) {
        return new Omsorgsovertakelse(
            barn.antallBarn(),
            barn.foreldreansvarsdato(),
            barn.fødselsdatoer(),
            tilVedleggsreferanse(barn.vedleggreferanser())
        );
    }

    private static Adopsjon tilAdopsjon(AdopsjonDto barn) {
        return new Adopsjon(
            barn.antallBarn(),
            barn.adopsjonsdato(),
            barn.adopsjonAvEktefellesBarn(),
            barn.søkerAdopsjonAlene() != null && barn.søkerAdopsjonAlene(),
            tilVedleggsreferanse(barn.vedleggreferanser()),
            barn.ankomstdato(),
            barn.fødselsdatoer()
        );
    }
}
