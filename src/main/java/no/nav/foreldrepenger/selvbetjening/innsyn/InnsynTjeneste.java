package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;

@Service
public class InnsynTjeneste implements Innsyn {

    private final InnsynConnection connectionFpinfo;
    private final OversiktConnection connectionFpoversikt;

    private static final Logger LOG = LoggerFactory.getLogger(InnsynTjeneste.class);
    private static final Logger SECURE_LOGGER = LoggerFactory.getLogger("secureLogger");

    public InnsynTjeneste(InnsynConnection connectionFpinfo,
                          OversiktConnection connectionFpoversikt) {
        this.connectionFpinfo = connectionFpinfo;
        this.connectionFpoversikt = connectionFpoversikt;
    }

    @Override
    public Saker hentSaker() {
        LOG.info("Henter saker for pålogget bruker");
        var sakerFraFpinfo = connectionFpinfo.hentSaker();
        sammenlignSakerFraOversiktOgFpinfoFailSafe(sakerFraFpinfo);
        return sakerFraFpinfo;
    }

    private void sammenlignSakerFraOversiktOgFpinfoFailSafe(Saker sakerFraFpinfo) {
        try {
            LOG.info("Henter saker for pålogget bruker fra fpoversikt");
            var sakerFraFpoversikt = connectionFpoversikt.hentSaker();

            if (sakerFraFpinfo.equals(sakerFraFpoversikt)) {
                LOG.info("Ingen avvik funnet ved sammenligning av saker hentet fra fpinfo og fpoversikt");
            } else {
                LOG.info("Avvik funnet ved sammenligning av saker hentet fra fpinfo og fpoversikt");
                SECURE_LOGGER.info("""
                    Avvik funnet ved sammenligning av saker hentet fra fpinfo og fpoversikt.
                    Fpinfo {}
                    Fpoversikt {}
                    """, sakerFraFpinfo, sakerFraFpoversikt);
            }
        } catch (Exception e) {
            LOG.info("Noe gikk galt med henting eller sammenligning av saker fra fpoversikt", e);
        }
    }

    @Override
    public Optional<AnnenPartVedtak> annenPartVedtak(AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        var annenPartVedtakFpinfo = connectionFpinfo.annenPartVedtak(annenPartVedtakIdentifikator);
        sammenlignAnnenpartsVedtakFraOversiktOgFpinfoFailSafe(annenPartVedtakIdentifikator, annenPartVedtakFpinfo);
        return annenPartVedtakFpinfo;
    }

    private void sammenlignAnnenpartsVedtakFraOversiktOgFpinfoFailSafe(AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator, Optional<AnnenPartVedtak> annenPartVedtakFpinfo) {
        try {
            LOG.info("Henter vedtak for annenpart fra fpoversikt");
            var annenpartsVedtakFpoversikt = connectionFpoversikt.hentAnnenpartsVedtak(annenPartVedtakIdentifikator);
            if (annenPartVedtakFpinfo.isEmpty() && annenpartsVedtakFpoversikt.isEmpty()) {
                LOG.info("Ingen avvik funnet ved sammenligning av annenparts vedtak hentet fra fpinfo og fpoversikt. Begge returnerer null");
            } else if (annenPartVedtakFpinfo.isEmpty() || annenpartsVedtakFpoversikt.isEmpty()) {
                if (annenPartVedtakFpinfo.isPresent()) {
                    LOG.info("Avvik i annenparts vedtak! Fpinfo returnerer vedtak mens fpoversikt returnerer null");
                } else {
                    LOG.info("Avvik i annenparts vedtak! Fpoversikt returnerer vedtak mens fpinfo returnerer null");
                }
            } else if (annenPartVedtakFpinfo.get().equals(annenpartsVedtakFpoversikt.get())) {
                LOG.info("Ingen avvik funnet ved sammenligning av annenparts vedtak hentet fra fpinfo og fpoversikt");
            } else {
                LOG.info("Avvik i annenparts vedtak! Innhold er ulikt. Sjekk secure logs for mer info");
                SECURE_LOGGER.info("""
                    Avvik i annenparts vedtak!
                    Fpinfo {}
                    Fpoversikt {}
                    """, annenPartVedtakFpinfo.get(), annenpartsVedtakFpoversikt.get());
            }
        } catch (Exception e) {
            LOG.info("Noe gikk galt med henting eller sammenligning av annenparts vedtak fra fpoversikt", e);
        }
    }

    @Override
    public String ping() {
        return connectionFpinfo.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsynConnection=" + connectionFpinfo + "]";
    }
}
