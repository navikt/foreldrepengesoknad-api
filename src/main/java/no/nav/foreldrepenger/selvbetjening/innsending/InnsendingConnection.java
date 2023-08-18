package no.nav.foreldrepenger.selvbetjening.innsending;

import static java.time.LocalDateTime.now;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.CommonMapper.tilVedlegg;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.EttersendingMapper.tilEttersending;
import static no.nav.foreldrepenger.selvbetjening.innsending.mapper.SøknadMapper.tilSøknad;
import static no.nav.foreldrepenger.selvbetjening.util.StringUtils.escapeHtml;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.common.domain.Kvittering;
import no.nav.foreldrepenger.common.domain.Søknad;
import no.nav.foreldrepenger.common.domain.felles.Vedlegg;
import no.nav.foreldrepenger.selvbetjening.http.AbstractRestConnection;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.EttersendingFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.SøknadFrontend;
import no.nav.foreldrepenger.selvbetjening.innsending.domain.VedleggFrontend;
import no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter;

@Component
public class InnsendingConnection extends AbstractRestConnection {
    private static final Logger LOG = LoggerFactory.getLogger(InnsendingConnection.class);
    private static final Logger SECURE_LOGGER = LoggerFactory.getLogger("secureLogger");

    private final InnsendingConfig config;
    private final Image2PDFConverter converter;

    public InnsendingConnection(RestOperations operations, InnsendingConfig config, Image2PDFConverter converter) {
        super(operations);
        this.config = config;
        this.converter = converter;
    }

    public Kvittering sendInn(SøknadFrontend søknad) {
        søknad.setOpprettet(now());
        return post(config.innsendingURI(), body(søknad));
    }

    public Kvittering ettersend(EttersendingFrontend ettersending) {
        return post(config.ettersendingURI(), body(ettersending));
    }

    public Kvittering endre(SøknadFrontend søknad) {
        return post(config.endringURI(), body(søknad));
    }

    private Kvittering post(URI uri, Object body) {
        return postForObject(uri, body, Kvittering.class);
    }

    public Søknad body(SøknadFrontend søknadFrontend) {
        if (LOG.isInfoEnabled()) {
            SECURE_LOGGER.info("{} mottatt fra frontend med følende innhold: {}", søknadFrontend.getType(), escapeHtml(søknadFrontend));
        }
        var dto = tilSøknad(søknadFrontend);

        var unikeVedleggMedInnhold = hentUnikeVedleggMedInnhold(søknadFrontend.getVedlegg());
        dto.getVedlegg().addAll(unikeVedleggMedInnhold);

        if (søknadFrontend.getVedlegg().size() > unikeVedleggMedInnhold.size()) {
            LOG.info("Mottatt duplikate vedlegg fra frontend ved innsending av søknad. Fjerner duplikate vedlegg. Sjekk secure logg for mer info.");
        }
        return dto;
    }

    public no.nav.foreldrepenger.common.domain.felles.Ettersending body(EttersendingFrontend ettersending) {
        var dto = tilEttersending(ettersending);

        var unikeVedleggMedInnhold = hentUnikeVedleggMedInnhold(ettersending.vedlegg());
        dto.vedlegg().addAll(unikeVedleggMedInnhold);

        if (ettersending.vedlegg().size() > unikeVedleggMedInnhold.size()) {
            LOG.info("Mottatt duplikate vedlegg under ettersending. Fjerner duplikate vedlegg. Sjekk secure logg for mer info.");
            if (LOG.isInfoEnabled()) {
                SECURE_LOGGER.info("Ettersendte vedlegg fra frontend før vasking er {}", escapeHtml(ettersending.vedlegg()));
            }
        }
        return dto;
    }

    private List<Vedlegg> hentUnikeVedleggMedInnhold(List<VedleggFrontend> vedleggFrontend) {
        return safeStream(vedleggFrontend).distinct().map(v -> tilVedlegg(convert(v))).toList();
    }

    private VedleggFrontend convert(VedleggFrontend v) {
        var vedlegg = v.kopi();
        if ((v.getContent() != null) && (v.getContent().length > 0)) {
            vedlegg.setContent(converter.convert(v.getContent()));
        }
        return vedlegg;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [config=" + config + "]";
    }

}
