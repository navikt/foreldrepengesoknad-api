package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.mapper;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedleggsreferanse;

import java.util.List;

import no.nav.foreldrepenger.common.domain.felles.LukketPeriode;
import no.nav.foreldrepenger.common.domain.felles.medlemskap.Utenlandsopphold;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Adopsjon;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.FremtidigFødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Fødsel;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.Omsorgsovertakelse;
import no.nav.foreldrepenger.common.domain.felles.relasjontilbarn.RelasjonTilBarn;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.DokumentasjonReferanseMapper;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.AdopsjonDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.FødselDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.OmsorgsovertakelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.TerminDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.v2.dto.UtenlandsoppholdsperiodeDto;

public final class CommonMapper {

    private CommonMapper() {
    }

    static Utenlandsopphold tilOppholdIUtlandet(SøknadDto s) {
        return new Utenlandsopphold(tilUtenlandsoppholds(s.utenlandsopphold()));
    }

    private static List<Utenlandsopphold.Opphold> tilUtenlandsoppholds(List<UtenlandsoppholdsperiodeDto> perioder) {
        return safeStream(perioder)
            .map(CommonMapper::tilUtenlandsopphold)
            .toList();
    }

    private static Utenlandsopphold.Opphold tilUtenlandsopphold(UtenlandsoppholdsperiodeDto oppholdsperiode) {
        return new Utenlandsopphold.Opphold(oppholdsperiode.landkode(), new LukketPeriode(oppholdsperiode.fom(), oppholdsperiode.tom()));
    }

    static RelasjonTilBarn tilRelasjonTilBarn(BarnDto barn, List<VedleggDto> vedlegg) {
        if (barn instanceof FødselDto f) {
            return tilFødsel(f, vedlegg);
        } else if (barn instanceof TerminDto t) {
            return tilFremtidigFødsel(t, vedlegg);
        } else if (barn instanceof AdopsjonDto a) {
            return tilAdopsjon(a, vedlegg);
        } else if (barn instanceof OmsorgsovertakelseDto o) {
            return tilOmsorgsovertagelse(o, vedlegg);
        } else {
            throw new IllegalStateException("Utviklerfeil: Skal ikke være mulig med annen type barn enn fødsel, termin, adopsjon eller omsorgsovertakelse");
        }
    }

    private static Fødsel tilFødsel(FødselDto barn, List<VedleggDto> vedlegg) {
        return new Fødsel(
            barn.antallBarn(),
            List.of(barn.fødselsdato()), // TODO: Fjern liste i mottak!
            barn.termindato(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg))
        );
    }

    private static FremtidigFødsel tilFremtidigFødsel(TerminDto barn, List<VedleggDto> vedlegg) {
        return new FremtidigFødsel(
            barn.antallBarn(),
            barn.termindato(),
            barn.terminbekreftelseDato(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg))
        );
    }

    private static Omsorgsovertakelse tilOmsorgsovertagelse(OmsorgsovertakelseDto barn, List<VedleggDto> vedlegg) {
        return new Omsorgsovertakelse(
            barn.antallBarn(),
            barn.foreldreansvarsdato(),
            barn.fødselsdatoer(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg))
        );
    }

    private static Adopsjon tilAdopsjon(AdopsjonDto barn, List<VedleggDto> vedlegg) {
        return new Adopsjon(
            barn.antallBarn(),
            barn.adopsjonsdato(),
            barn.adopsjonAvEktefellesBarn(),
            barn.søkerAdopsjonAlene() != null && barn.søkerAdopsjonAlene(),
            tilVedleggsreferanse(DokumentasjonReferanseMapper.dokumentasjonSomDokumentererBarn(vedlegg)),
            barn.ankomstdato(),
            barn.fødselsdatoer()
        );
    }
}
