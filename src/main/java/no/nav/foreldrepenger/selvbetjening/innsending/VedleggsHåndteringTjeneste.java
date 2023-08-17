package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.util.stream.Stream.concat;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.ForeldrepengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.MutableVedleggReferanse;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SvangerskapspengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggFrontend;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;

@Component
public class VedleggsHåndteringTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(VedleggsHåndteringTjeneste.class);
    private final Image2PDFConverter converter;

    public VedleggsHåndteringTjeneste(Image2PDFConverter converter) {
        this.converter = converter;
    }

    public EttersendingFrontend fjernDupliserteVedlegg(EttersendingFrontend ettersending) {
        var alleVedlegg = ettersending.vedlegg();
        var unikeVedlegg = alleVedlegg.stream()
                .distinct()
                .toList();

        ettersending.vedlegg(unikeVedlegg);

        LOG.info("Fjerner {} dupliserte av totalt {} mottatt.", alleVedlegg.size() - unikeVedlegg.size(), alleVedlegg.size());
        return ettersending;
    }

    public SøknadFrontend fjernDupliserteVedlegg(SøknadFrontend søknad) {
        if (søknad.getVedlegg().isEmpty()) {
            return søknad;
        }
        var duplisertVedleggReferanseTilEksisterendeVedleggReferanseMapping = new HashMap<MutableVedleggReferanse, MutableVedleggReferanse>();
        var unikeVedlegg = new ArrayList<VedleggFrontend>();
        var alleVedleggMedInnhold = hentUnikeVedleggMedInnhold(søknad.getVedlegg());

        for (var vedlegg : alleVedleggMedInnhold) {
            var vedleggEksistererAllerede = unikeVedlegg.stream()
                    .filter(v -> Objects.equals(vedlegg.getInnsendingsType(), v.getInnsendingsType()))
                    .filter(v -> Objects.equals(vedlegg.getSkjemanummer(), v.getSkjemanummer()))
                    .filter(v -> Arrays.equals(vedlegg.getContent(), v.getContent()))
                    .findFirst();

            if (vedleggEksistererAllerede.isPresent()) {
                duplisertVedleggReferanseTilEksisterendeVedleggReferanseMapping.put(vedlegg.getId(), vedleggEksistererAllerede.get().getId());
            } else {
                unikeVedlegg.add(vedlegg);
            }
        }

        erstattAlleReferanserSomErDuplikater(søknad, duplisertVedleggReferanseTilEksisterendeVedleggReferanseMapping);
        erstattGammelVedleggslisteMedNyUtenDuplikater(søknad, unikeVedlegg);
        LOG.info("Fjerner {} dupliserte vedlegg av totalt {} mottatt.", alleVedleggMedInnhold.size() - unikeVedlegg.size(), alleVedleggMedInnhold.size());
        return søknad;
    }

    private static void erstattGammelVedleggslisteMedNyUtenDuplikater(SøknadFrontend søknad, ArrayList<VedleggFrontend> unikeVedlegg) {
        søknad.setVedlegg(unikeVedlegg.stream().toList());
    }

    private static void erstattAlleReferanserSomErDuplikater(SøknadFrontend søknad, HashMap<MutableVedleggReferanse, MutableVedleggReferanse> nyReferanseMapping) {
        for (var gammelReferanse : nyReferanseMapping.entrySet()) {
            var søker = søknad.getSøker();
            søker.selvstendigNæringsdrivendeInformasjon().stream()
                    .flatMap(sn -> sn.vedlegg().stream())
                    .filter(v -> v.equals(gammelReferanse.getKey()))
                    .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));

            søker.andreInntekterSiste10Mnd().stream()
                    .flatMap(a -> a.vedlegg().stream())
                    .filter(v -> v.equals(gammelReferanse.getKey()))
                    .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));

            var barn = søknad.getBarn();
            concat(barn.terminbekreftelse().stream(),
                    concat(barn.adopsjonsvedtak().stream(),
                    concat(barn.omsorgsovertakelse().stream(), barn.dokumentasjonAvAleneomsorg().stream())))
                    .filter(v -> v.equals(gammelReferanse.getKey()))
                    .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));

            if (søknad instanceof ForeldrepengesøknadFrontend fpSøknad) {
                fpSøknad.getUttaksplan().stream()
                        .flatMap(uttaksplanPeriode -> uttaksplanPeriode.vedlegg().stream())
                        .filter(v -> v.equals(gammelReferanse.getKey()))
                        .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));
            } else if (søknad instanceof SvangerskapspengesøknadFrontend svpSøknad) {
                svpSøknad.getTilrettelegging().stream()
                        .flatMap(t -> t.vedlegg().stream())
                        .filter(v -> v.equals(gammelReferanse.getKey()))
                        .forEach(v -> v.referanse(gammelReferanse.getValue().referanse()));
            }
        }
    }

    private List<VedleggFrontend> hentUnikeVedleggMedInnhold(List<VedleggFrontend> vedleggFrontend) {
        return safeStream(vedleggFrontend).distinct().map(this::convert).toList();
    }

    private VedleggFrontend convert(VedleggFrontend v) {
        VedleggFrontend vedlegg = v.kopi();
        if ((v.getContent() != null) && (v.getContent().length > 0)) {
            vedlegg.setContent(converter.convert(v.getContent()));
        }
        return vedlegg;
    }

}
