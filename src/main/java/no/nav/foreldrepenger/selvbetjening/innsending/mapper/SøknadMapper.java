package no.nav.foreldrepenger.selvbetjening.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EngangsstønadMapper.tilEngangsstønad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.ForeldrepengerMapper.tilForeldrepengesøknad;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SvangerskapspengerMapper.tilSvangerskapspengesøknad;

import no.nav.foreldrepenger.common.domain.felles.DokumentType;
import no.nav.foreldrepenger.common.domain.felles.InnsendingsType;
import no.nav.foreldrepenger.common.domain.felles.PåkrevdVedlegg;
import no.nav.foreldrepenger.common.domain.felles.VedleggMetaData;
import no.nav.foreldrepenger.selvbetjening.error.UnexpectedInputException;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Foreldrepengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Svangerskapspengesøknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Søknad;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.Vedlegg;

public final class SøknadMapper {

    private SøknadMapper() {
    }

    public static no.nav.foreldrepenger.common.domain.Søknad tilSøknad(Søknad søknad) {
        return switch (søknad) {
            case Engangsstønad e -> tilEngangsstønad(e);
            case Foreldrepengesøknad f -> tilForeldrepengesøknad(f);
            case Svangerskapspengesøknad s -> tilSvangerskapspengesøknad(s);
            default -> throw new UnexpectedInputException("Unknown application type " + søknad.getClass().getSimpleName());
        };
    }

    public static no.nav.foreldrepenger.common.domain.felles.Vedlegg tilVedlegg(Vedlegg vedlegg) {
        var vedleggMetadata = new VedleggMetaData(
            vedlegg.getBeskrivelse(),
            vedlegg.getId(),
            vedlegg.getInnsendingsType() != null ? InnsendingsType.valueOf(vedlegg.getInnsendingsType()) : null,
            vedlegg.getSkjemanummer() != null ? DokumentType.valueOf(vedlegg.getSkjemanummer()) : null
        );
        return new PåkrevdVedlegg(vedleggMetadata, vedlegg.getContent());
    }
}
