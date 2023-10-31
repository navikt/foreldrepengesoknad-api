package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.util.stream.Stream.concat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.Innsending;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.MutableVedleggReferanseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøkerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.SøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.VedleggDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.endringssøknad.EndringssøknadForeldrepengerDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.SøknadV2Dto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.ettersendelse.EttersendelseDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.ForeldrepengesøknadDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.foreldrepenger.UttaksplanPeriodeDto;
import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.svangerskapspenger.SvangerskapspengesøknadDto;

public final class VedleggsHåndteringTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(VedleggsHåndteringTjeneste.class);

    private VedleggsHåndteringTjeneste() {
        // Skal ikke instansieres
    }

    public static void fjernDupliserteVedleggFraInnsending(Innsending innsending) {
        var antallVedleggFørDuplikatsjekk = innsending.vedlegg().size();
        var duplikatTilEksisterende = finnOgfjernDupliserteVedlegg(innsending.vedlegg());
        if (innsending instanceof SøknadDto søknad) {
            erstattAlleReferanserSomErDuplikater(søknad, duplikatTilEksisterende);
        } else if (innsending instanceof SøknadV2Dto søknadV2) {
            erstattAlleReferanserSomErDuplikater(søknadV2, duplikatTilEksisterende);
        } else if (innsending instanceof EndringssøknadDto endringssøknad) {
            erstattAlleReferanserSomErDuplikater(endringssøknad, duplikatTilEksisterende);
        } else if (innsending instanceof EttersendelseDto) {
            // Ingen referanser å fjerner for ettersendelse
        } else {
            throw new IllegalStateException("Utviklerfeil: Innsending er hverken av type søknad, endring eller ettersendelse!");
        }

        LOG.info("Fjernet {} dupliserte vedlegg av totalt {} mottatt",  antallVedleggFørDuplikatsjekk - innsending.vedlegg().size(), antallVedleggFørDuplikatsjekk);
    }

    private static Map<MutableVedleggReferanseDto, MutableVedleggReferanseDto> finnOgfjernDupliserteVedlegg(List<VedleggDto> vedlegg) {
        if (vedlegg.isEmpty()) {
            return Map.of();
        }
        var duplikatTilEksisterende = finnDupliserteVedlegg(vedlegg);
        vedlegg.removeIf(vedleggDto -> duplikatTilEksisterende.containsKey(vedleggDto.getId()));
        return duplikatTilEksisterende;
    }

    private static Map<MutableVedleggReferanseDto, MutableVedleggReferanseDto> finnDupliserteVedlegg(List<VedleggDto> vedlegg) {
        var kopi = new ArrayList<>(vedlegg);
        var duplikatTilEksisterende = new HashMap<MutableVedleggReferanseDto, MutableVedleggReferanseDto>();
        for (var gjeldendeVedlegg : vedlegg) {
            var eksisterende = kopi.stream()
                .filter(v -> !Objects.equals(gjeldendeVedlegg.getId(), v.getId()))
                .filter(v -> Objects.equals(gjeldendeVedlegg.getInnsendingsType(), v.getInnsendingsType()))
                .filter(v -> Objects.equals(gjeldendeVedlegg.getSkjemanummer(), v.getSkjemanummer()))
                .filter(v -> sizeEquals(gjeldendeVedlegg, v))
                .findFirst();
            if (eksisterende.isPresent()) {
                duplikatTilEksisterende.putIfAbsent(gjeldendeVedlegg.getId(), eksisterende.get().getId());
                duplikatTilEksisterende.replaceAll((key, value) -> {
                    if (value.equals(gjeldendeVedlegg.getId())) {
                        return eksisterende.get().getId();
                    } else {
                        return value;
                    }});
                kopi.remove(gjeldendeVedlegg);
            }
        }
        return duplikatTilEksisterende;
    }

    private static boolean sizeEquals(VedleggDto vedlegg, VedleggDto kandidat) {
        var vedleggLength = vedlegg.getContent() != null ? vedlegg.getContent().length : 0;
        var kandidatLength = kandidat.getContent() != null ? kandidat.getContent().length : 0;
        return Objects.equals(vedleggLength, kandidatLength);
    }

    private static void erstattAlleReferanserSomErDuplikater(EndringssøknadDto endringssøknadDto, Map<MutableVedleggReferanseDto, MutableVedleggReferanseDto> nyReferanseMapping) {
        for (var gammelReferanse : nyReferanseMapping.entrySet()) {
            if (endringssøknadDto instanceof EndringssøknadForeldrepengerDto fpEndring) {
                erstattReferanserSøker(gammelReferanse, fpEndring.søker());
                erstattReferanserUttaksplan(gammelReferanse, fpEndring.uttaksplan());
            }
        }
    }

    private static void erstattAlleReferanserSomErDuplikater(SøknadV2Dto søknad, Map<MutableVedleggReferanseDto, MutableVedleggReferanseDto> nyReferanseMapping) {
        for (var gammelReferanse : nyReferanseMapping.entrySet()) {
            erstattReferanserBarn(gammelReferanse, søknad.barn());
        }
    }

    private static void erstattAlleReferanserSomErDuplikater(SøknadDto søknad, Map<MutableVedleggReferanseDto, MutableVedleggReferanseDto> nyReferanseMapping) {
        for (var gammelReferanse : nyReferanseMapping.entrySet()) {
            erstattReferanserSøker(gammelReferanse, søknad.søker());
            erstattReferanserBarn(gammelReferanse, søknad.barn());

            if (søknad instanceof ForeldrepengesøknadDto fpSøknad) {
                erstattReferanserUttaksplan(gammelReferanse, fpSøknad.uttaksplan());
            } else if (søknad instanceof SvangerskapspengesøknadDto svpSøknad) {
                svpSøknad.tilrettelegging().stream()
                        .flatMap(t -> t.vedlegg().stream())
                        .filter(v -> v.equals(gammelReferanse.getKey()))
                        .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));
            }
        }
    }

    private static void erstattReferanserUttaksplan(Map.Entry<MutableVedleggReferanseDto, MutableVedleggReferanseDto> gammelReferanse, List<UttaksplanPeriodeDto> uttaksplan) {
        uttaksplan.stream()
                .flatMap(uttaksplanPeriode -> uttaksplanPeriode.vedlegg().stream())
                .filter(v -> v.equals(gammelReferanse.getKey()))
                .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));
    }

    private static void erstattReferanserBarn(Map.Entry<MutableVedleggReferanseDto, MutableVedleggReferanseDto> gammelReferanse, BarnDto barn) {
        concat(barn.terminbekreftelse().stream(),
                concat(barn.adopsjonsvedtak().stream(),
                concat(barn.omsorgsovertakelse().stream(), barn.dokumentasjonAvAleneomsorg().stream())))
                .filter(v -> v.equals(gammelReferanse.getKey()))
                .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));
    }

    private static void erstattReferanserBarn(Map.Entry<MutableVedleggReferanseDto, MutableVedleggReferanseDto> gammelReferanse, no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.engangsstønad.BarnDto barn) {
        barn.vedleggreferanser().stream()
            .filter(v -> v.equals(gammelReferanse.getKey()))
            .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));
    }

    private static void erstattReferanserSøker(Map.Entry<MutableVedleggReferanseDto, MutableVedleggReferanseDto> gammelReferanse, SøkerDto søker) {
        søker.selvstendigNæringsdrivendeInformasjon().stream()
                .flatMap(sn -> sn.vedlegg().stream())
                .filter(v -> v.equals(gammelReferanse.getKey()))
                .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));

        søker.andreInntekterSiste10Mnd().stream()
                .flatMap(a -> a.vedlegg().stream())
                .filter(v -> v.equals(gammelReferanse.getKey()))
                .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));
    }
}
