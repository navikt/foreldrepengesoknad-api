package no.nav.foreldrepenger.selvbetjening.innsending;

import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.ForeldrepengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.MutableVedleggReferanse;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SvangerskapspengesøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggFrontend;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Stream.concat;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;

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
        var alleVedlegg = søknad.getVedlegg();
        if (alleVedlegg.isEmpty()) {
            return søknad;
        }

        var duplisertVedleggReferanseTilEksisterendeVedleggReferanseMapping = new HashMap<MutableVedleggReferanse, MutableVedleggReferanse>();
        var unikeVedlegg = new ArrayList<VedleggFrontend>();

        long start = System.currentTimeMillis();
        for (var vedlegg : alleVedlegg) {
            var vedleggEksistererAllerede = unikeVedlegg.stream()
                    .filter(v -> Objects.equals(vedlegg.getInnsendingsType(), v.getInnsendingsType()))
                    .filter(v -> Objects.equals(vedlegg.getSkjemanummer(), v.getSkjemanummer()))
                    .filter(v -> Objects.equals(vedlegg.getFilesize(), v.getFilesize()))
                    .filter(v -> Arrays.equals(vedlegg.getContent(), v.getContent()))
                    .findFirst();

            if (vedleggEksistererAllerede.isPresent()) {
                duplisertVedleggReferanseTilEksisterendeVedleggReferanseMapping.put(vedlegg.getId(), vedleggEksistererAllerede.get().getId());
            } else {
                unikeVedlegg.add(vedlegg);
            }
        }

        erstattAlleReferanserSomErDuplikater(søknad, duplisertVedleggReferanseTilEksisterendeVedleggReferanseMapping);
        long slutt = System.currentTimeMillis();
        LOG.info("Fjerner {} dupliserte vedlegg av totalt {} mottatt ({}ms).", alleVedlegg.size() - unikeVedlegg.size(), alleVedlegg.size(), slutt - start);
        søknad.setVedlegg(konverterTilPDF(unikeVedlegg));
        return søknad;
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

    private List<VedleggFrontend> konverterTilPDF(List<VedleggFrontend> vedleggFrontend) {
        long start = System.currentTimeMillis();
        var unikeVedleggMedInnhold = safeStream(vedleggFrontend).distinct().map(this::convert).toList();
        long slutt = System.currentTimeMillis();
        LOG.info("Konvertering av vedlegg til PDF tok {}ms", slutt - start);
        return unikeVedleggMedInnhold;
    }

    private VedleggFrontend convert(VedleggFrontend v) {
        VedleggFrontend vedlegg = v.kopi();
        if ((v.getContent() != null) && (v.getContent().length > 0)) {
            vedlegg.setContent(converter.convert(v.getContent()));
        }
        return vedlegg;
    }

}
