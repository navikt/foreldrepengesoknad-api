package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EngangsstønadMapper.tilEngangsstønad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.ForeldrepengerMapper.tilForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SvangerskapspengerMapper.tilSvangerskapspengesøknad;

import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.InnsendingsType;
import no.nav.foreldrepenger.common.domain.felles.PåkrevdVedlegg;
import no.nav.foreldrepenger.common.domain.felles.VedleggMetaData;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EngangsstønadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.ForeldrepengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SvangerskapspengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggFrontend;

public final class SøknadMapper {

    private SøknadMapper() {
    }

    public static no.nav.foreldrepenger.common.domain.Søknad tilSøknad(SøknadFrontend søknad) {
        return switch (søknad) {
            case EngangsstønadFrontend e -> tilEngangsstønad(e);
            case ForeldrepengesøknadFrontend f -> tilForeldrepengesøknad(f);
            case SvangerskapspengesøknadFrontend s -> tilSvangerskapspengesøknad(s);
        };
    }

    public static no.nav.foreldrepenger.common.domain.felles.Vedlegg tilVedlegg(VedleggFrontend vedlegg) {
        var vedleggMetadata = new VedleggMetaData(
            vedlegg.getBeskrivelse(),
            vedlegg.getId(),
            vedlegg.getInnsendingsType() != null ? InnsendingsType.valueOf(vedlegg.getInnsendingsType()) : null,
            vedlegg.getSkjemanummer() != null ? DokumentType.valueOf(vedlegg.getSkjemanummer()) : null
        );
        return new PåkrevdVedlegg(vedleggMetadata, vedlegg.getContent());
    }
}
