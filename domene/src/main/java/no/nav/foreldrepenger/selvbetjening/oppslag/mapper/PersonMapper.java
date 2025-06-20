package no.nav.foreldrepenger.selvbetjening.oppslag.mapper;

import static java.util.Comparator.comparing;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.domain.felles.AnnenPart;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.AnnenForelderFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.Bankkonto;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.BarnFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.PersonFrontend;
import no.nav.foreldrepenger.selvbetjening.oppslag.dto.Sivilstand;

public final class PersonMapper {

    private static final Logger LOG = LoggerFactory.getLogger(PersonMapper.class);

    private PersonMapper() {
    }

    // TODO: Fjern unødvendig mapper og DTO-er... må skrives litt om i frontend
    public static PersonFrontend tilPersonFrontend(no.nav.foreldrepenger.common.domain.felles.Person dto) {
        return new PersonFrontend(dto.fnr(),
            Optional.ofNullable(dto.navn()).map(no.nav.foreldrepenger.common.domain.Navn::fornavn).orElse(null),
            Optional.ofNullable(dto.navn()).map(no.nav.foreldrepenger.common.domain.Navn::mellomnavn).orElse(null),
            Optional.ofNullable(dto.navn()).map(no.nav.foreldrepenger.common.domain.Navn::etternavn).orElse(null),
            Optional.ofNullable(dto.kjønn()).orElse(null),
            dto.fødselsdato(),
            tilBankkonto(dto.bankkonto()),
            sort(tilBarn(dto.barn())),
            tilSivilstand(dto.sivilstand()));

    }

    private static Sivilstand tilSivilstand(no.nav.foreldrepenger.common.domain.felles.Sivilstand sivilstand) {
        return sivilstand == null ? null : new Sivilstand(map(sivilstand.type()));
    }

    private static Sivilstand.SivilstandType map(no.nav.foreldrepenger.common.domain.felles.Sivilstand.SivilstandType type) {
        return switch (type) {
            case UOPPGITT -> Sivilstand.SivilstandType.UOPPGITT;
            case UGIFT -> Sivilstand.SivilstandType.UGIFT;
            case GIFT -> Sivilstand.SivilstandType.GIFT;
            case ENKE_ELLER_ENKEMANN -> Sivilstand.SivilstandType.ENKE_ELLER_ENKEMANN;
            case SKILT -> Sivilstand.SivilstandType.SKILT;
            case SEPARERT -> Sivilstand.SivilstandType.SEPARERT;
            case REGISTRERT_PARTNER -> Sivilstand.SivilstandType.REGISTRERT_PARTNER;
            case SEPARERT_PARTNER -> Sivilstand.SivilstandType.SEPARERT_PARTNER;
            case SKILT_PARTNER -> Sivilstand.SivilstandType.SKILT_PARTNER;
            case GJENLEVENDE_PARTNER -> Sivilstand.SivilstandType.GJENLEVENDE_PARTNER;
        };
    }

    private static Bankkonto tilBankkonto(no.nav.foreldrepenger.common.domain.felles.Bankkonto bankkonto) {
        return bankkonto == null ? null : new Bankkonto(bankkonto.kontonummer(), bankkonto.banknavn());
    }

    private static List<BarnFrontend> tilBarn(List<no.nav.foreldrepenger.common.domain.Barn> barn) {
        return barn.stream().map(PersonMapper::tilBarn).toList();
    }

    private static BarnFrontend tilBarn(no.nav.foreldrepenger.common.domain.Barn barn) {
        return new BarnFrontend(Optional.ofNullable(barn.fnr()).map(Fødselsnummer::value).orElse(null),
            Optional.ofNullable(barn.navn()).map(no.nav.foreldrepenger.common.domain.Navn::fornavn).orElse(null),
            Optional.ofNullable(barn.navn()).map(no.nav.foreldrepenger.common.domain.Navn::mellomnavn).orElse(null),
            Optional.ofNullable(barn.navn()).map(no.nav.foreldrepenger.common.domain.Navn::etternavn).orElse(null),
            Optional.ofNullable(barn.kjønn()).orElse(null),
            barn.fødselsdato(),
            barn.dødsdato(),
            tilAnnenforelder(barn.annenPart()));
    }

    private static AnnenForelderFrontend tilAnnenforelder(AnnenPart annenPart) {
        if (annenPart == null) {
            return null;
        }
        return new AnnenForelderFrontend(annenPart.fnr().value(),
            Optional.ofNullable(annenPart.navn()).map(no.nav.foreldrepenger.common.domain.Navn::fornavn).orElse(null),
            Optional.ofNullable(annenPart.navn()).map(no.nav.foreldrepenger.common.domain.Navn::mellomnavn).orElse(null),
            Optional.ofNullable(annenPart.navn()).map(no.nav.foreldrepenger.common.domain.Navn::etternavn).orElse(null),
            annenPart.fødselsdato());

    }

    private static List<BarnFrontend> sort(List<BarnFrontend> barn) {
        try {
            return safeStream(barn).sorted(comparing(BarnFrontend::fødselsdato)).toList();
        } catch (Exception e) {
            LOG.warn("Feil ved sortering", e);
            return barn;
        }
    }
}
