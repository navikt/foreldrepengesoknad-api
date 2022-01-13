package no.nav.foreldrepenger.selvbetjening.oppslag.mapper;

import static java.util.Comparator.comparing;
import static no.nav.foreldrepenger.selvbetjening.util.IkkeNordiskEØSLand.ikkeNordiskEøsLand;
import static no.nav.foreldrepenger.selvbetjening.util.StreamUtil.safeStream;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.common.domain.felles.AnnenPart;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.AnnenForelderFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.BarnFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.PersonFrontend;

public final class PersonMapper {

    private static final Logger LOG = LoggerFactory.getLogger(PersonMapper.class);

    private PersonMapper() {
    }

    public static PersonFrontend tilPersonFrontend(no.nav.foreldrepenger.common.domain.felles.Person dto) {
        return new PersonFrontend(
            dto.fnr().getFnr(),
            Optional.ofNullable(dto.navn()).map(no.nav.foreldrepenger.common.domain.Navn::fornavn).orElse(null),
            Optional.ofNullable(dto.navn()).map(no.nav.foreldrepenger.common.domain.Navn::mellomnavn).orElse(null),
            Optional.ofNullable(dto.navn()).map(no.nav.foreldrepenger.common.domain.Navn::etternavn).orElse(null),
            Optional.ofNullable(dto.kjønn()).map(Enum::name).orElse(null),
            dto.fødselsdato(),
            ikkeNordiskEøsLand(dto.land()),
            dto.bankkonto(),
            sort(tilBarn(dto.barn()))
        );

    }

    private static List<BarnFrontend> tilBarn(Set<no.nav.foreldrepenger.common.domain.Barn> barn) {
        return barn.stream()
            .map(PersonMapper::tilBarn)
            .toList();
    }

    private static BarnFrontend tilBarn(no.nav.foreldrepenger.common.domain.Barn barn) {
        return new BarnFrontend(
            barn.fnr().getFnr(),
            Optional.ofNullable(barn.navn()).map(no.nav.foreldrepenger.common.domain.Navn::fornavn).orElse(null),
            Optional.ofNullable(barn.navn()).map(no.nav.foreldrepenger.common.domain.Navn::mellomnavn).orElse(null),
            Optional.ofNullable(barn.navn()).map(no.nav.foreldrepenger.common.domain.Navn::etternavn).orElse(null),
            Optional.ofNullable(barn.kjønn()).map(Enum::name).orElse(null),
            barn.fødselsdato(), tilAnnenforelder(barn.annenPart()));
    }

    private static AnnenForelderFrontend tilAnnenforelder(AnnenPart annenPart) {
        if (annenPart == null) {
            return null;
        }
        return new AnnenForelderFrontend(annenPart.fnr().getFnr(),
            Optional.ofNullable(annenPart.navn()).map(no.nav.foreldrepenger.common.domain.Navn::fornavn).orElse(null),
            Optional.ofNullable(annenPart.navn()).map(no.nav.foreldrepenger.common.domain.Navn::mellomnavn).orElse(null),
            Optional.ofNullable(annenPart.navn()).map(no.nav.foreldrepenger.common.domain.Navn::etternavn).orElse(null),
            annenPart.fødselsdato());

    }

    private static List<BarnFrontend> sort(List<BarnFrontend> barn) {
        try {
            return safeStream(barn)
                .sorted(comparing(BarnFrontend::fødselsdato))
                .toList();
        } catch (Exception e) {
            LOG.warn("Feil ved sortering", e);
            return barn;
        }
    }
}
