package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.util.stream.Stream.concat;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.kontrakt.innsending.dto.BarnDto;
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
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;

@Component
public class VedleggsHåndteringTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(VedleggsHåndteringTjeneste.class);
    private final Image2PDFConverter converter;

    public VedleggsHåndteringTjeneste(Image2PDFConverter converter) {
        this.converter = converter;
    }

    public void fjernDupliserteVedleggFraEttersending(EttersendelseDto ettersending) {
        var antallVedleggFørDuplikatsjek = ettersending.vedlegg().size();
        if (antallVedleggFørDuplikatsjek == 0) {
            return;
        }

        finnOgfjernDupliserteVedlegg(ettersending.vedlegg());
        konverterTilPDF(ettersending.vedlegg());
        LOG.info("Fjerner {} dupliserte ettersendte vedlegg av totalt {} mottatt",  antallVedleggFørDuplikatsjek - ettersending.vedlegg().size(), antallVedleggFørDuplikatsjek);
    }

    public void fjernDupliserteVedleggFraSøknad(SøknadV2Dto søknad) {
        var antallVedleggFørDuplikatsjek = søknad.vedlegg().size();
        if (antallVedleggFørDuplikatsjek == 0) {
            return;
        }

        var duplikatTilEksisterende = finnOgfjernDupliserteVedlegg(søknad.vedlegg());
        erstattAlleReferanserSomErDuplikater(søknad, duplikatTilEksisterende);
        konverterTilPDF(søknad.vedlegg());
        LOG.info("Fjerner {} dupliserte vedlegg fra søknad av totalt {} mottatt", antallVedleggFørDuplikatsjek - søknad.vedlegg().size(), antallVedleggFørDuplikatsjek);
    }

    public void fjernDupliserteVedleggFraSøknad(SøknadDto søknad) {
        var antallVedleggFørDuplikatsjek = søknad.vedlegg().size();
        if (antallVedleggFørDuplikatsjek == 0) {
            return;
        }

        var duplikatTilEksisterende = finnOgfjernDupliserteVedlegg(søknad.vedlegg());
        erstattAlleReferanserSomErDuplikater(søknad, duplikatTilEksisterende);
        konverterTilPDF(søknad.vedlegg());
        LOG.info("Fjerner {} dupliserte vedlegg fra søknad av totalt {} mottatt", antallVedleggFørDuplikatsjek - søknad.vedlegg().size(), antallVedleggFørDuplikatsjek);
    }

    public void fjernDupliserteVedleggFraSøknad(EndringssøknadDto endringssøknad) {
        var antallVedleggFørDuplikatsjek = endringssøknad.vedlegg().size();
        if (antallVedleggFørDuplikatsjek == 0) {
            return;
        }

        var duplikatTilEksisterende = finnOgfjernDupliserteVedlegg(endringssøknad.vedlegg());
        erstattAlleReferanserSomErDuplikater(endringssøknad, duplikatTilEksisterende);
        konverterTilPDF(endringssøknad.vedlegg());
        LOG.info("Fjerner {} dupliserte vedlegg fra endringssøknad av totalt {} mottatt", antallVedleggFørDuplikatsjek - endringssøknad.vedlegg().size(), antallVedleggFørDuplikatsjek);
    }

    private HashMap<MutableVedleggReferanseDto, MutableVedleggReferanseDto> finnOgfjernDupliserteVedlegg(List<VedleggDto> vedlegg) {
        var duplikatTilEksisterende = finnDupliserteVedlegg(vedlegg);
        vedlegg.removeIf(vedleggDto -> duplikatTilEksisterende.containsKey(vedleggDto.getId()));
        return duplikatTilEksisterende;
    }

    private static HashMap<MutableVedleggReferanseDto, MutableVedleggReferanseDto> finnDupliserteVedlegg(List<VedleggDto> vedlegg) {
        var kopi = new ArrayList<>(vedlegg);
        var duplikatTilEksisterende = new HashMap<MutableVedleggReferanseDto, MutableVedleggReferanseDto>();
        for (var gjeldendeVedlegg : vedlegg) {
            var eksisterende = kopi.stream()
                .filter(v -> !Objects.equals(gjeldendeVedlegg.getId(), v.getId()))
                .filter(v -> Objects.equals(gjeldendeVedlegg.getInnsendingsType(), v.getInnsendingsType()))
                .filter(v -> Objects.equals(gjeldendeVedlegg.getSkjemanummer(), v.getSkjemanummer()))
                .filter(v -> sizeEquals(gjeldendeVedlegg, v))
                .filter(v -> Arrays.equals(gjeldendeVedlegg.getContent(), v.getContent()))
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

    private static void erstattAlleReferanserSomErDuplikater(EndringssøknadDto endringssøknadDto, HashMap<MutableVedleggReferanseDto, MutableVedleggReferanseDto> nyReferanseMapping) {
        for (var gammelReferanse : nyReferanseMapping.entrySet()) {
            if (endringssøknadDto instanceof EndringssøknadForeldrepengerDto fpEndring) {
                erstattReferanserSøker(gammelReferanse, fpEndring.søker());
                erstattReferanserUttaksplan(gammelReferanse, fpEndring.uttaksplan());
            }
        }
    }

    private static void erstattAlleReferanserSomErDuplikater(SøknadV2Dto søknad, HashMap<MutableVedleggReferanseDto, MutableVedleggReferanseDto> nyReferanseMapping) {
        for (var gammelReferanse : nyReferanseMapping.entrySet()) {
            erstattReferanserBarn(gammelReferanse, søknad.barn());
        }
    }

    private static void erstattAlleReferanserSomErDuplikater(SøknadDto søknad, HashMap<MutableVedleggReferanseDto, MutableVedleggReferanseDto> nyReferanseMapping) {
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
        barn.vedlegg().stream()
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

    private List<VedleggDto> konverterTilPDF(List<VedleggDto> vedleggFrontend) {
        long start = System.currentTimeMillis();
        var unikeVedleggMedInnhold = safeStream(vedleggFrontend).distinct().map(this::convert).toList();
        long slutt = System.currentTimeMillis();
        LOG.info("Konvertering av vedlegg til PDF tok {}ms", slutt - start);
        return unikeVedleggMedInnhold;
    }

    private VedleggDto convert(VedleggDto vedlegg) {
        var originalContent = vedlegg.getContent();
        if (originalContent != null && originalContent.length > 0) {
            vedlegg.setContent(converter.convert(originalContent));
        }
        return vedlegg;
    }

}
