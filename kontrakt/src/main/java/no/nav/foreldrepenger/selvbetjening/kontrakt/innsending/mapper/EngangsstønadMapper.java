package no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper;

import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilOppholdIUtlandet;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilRelasjonTilBarn;
import static no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.mapper.CommonMapper.tilVedlegg;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.common.domain.BrukerRolle;
import no.nav.foreldrepenger.common.domain.Søker;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.Ytelse;
import no.nav.foreldrepenger.common.domain.engangsstønad.Engangsstønad;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.EngangsstønadDto;

final class EngangsstønadMapper {

    private EngangsstønadMapper() {
    }

    static Søknad tilEngangsstønadVedleggUtenInnhold(EngangsstønadDto e, LocalDate mottattDato) {
        var vedlegg = e.vedlegg();
        return new Søknad(
            mottattDato,
            tilSøker(e.søker()),
            tilYtelse(e, vedlegg),
            null,
            tilVedlegg(vedlegg)
        );
    }

    private static Søker tilSøker(SøkerDto søker) {
        return new Søker(BrukerRolle.MOR, søker.språkkode()); // TODO: Frontend sender ikke ned søker her. Kan også være Far/Medmor!
    }

    private static Ytelse tilYtelse(EngangsstønadDto e, List<VedleggDto> vedlegg) {
        return new Engangsstønad(
            tilOppholdIUtlandet(e),
            tilRelasjonTilBarn(e.barn(), e.situasjon(), vedlegg)
        );
    }
}
