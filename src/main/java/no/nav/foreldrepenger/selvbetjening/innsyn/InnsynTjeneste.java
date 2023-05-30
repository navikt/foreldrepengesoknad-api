package no.nav.foreldrepenger.selvbetjening.innsyn;

import static no.nav.boot.conditionals.EnvUtil.isProd;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;

@Service
public class InnsynTjeneste implements Innsyn, EnvironmentAware {

    private Environment env;
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
            } else if (!isProd(env) || !harSaker(sakerFraFpoversikt)) {
                LOG.info("Avvik funnet ved sammenligning av saker hentet fra fpinfo og fpoversikt");
                SECURE_LOGGER.info("""
                    Avvik funnet ved sammenligning av saker hentet fra fpinfo og fpoversikt.
                    Fpinfo {}
                    Fpoversikt {}
                    """, sakerFraFpinfo, sakerFraFpoversikt);
            }
        } catch (Exception e) {
            LOG.warn("Noe gikk galt med henting eller sammenligning av saker fra fpoversikt", e);
        }
    }

    private boolean harSaker(Saker sakerFraFpoversikt) {
        return !sakerFraFpoversikt.foreldrepenger().isEmpty()
            || !sakerFraFpoversikt.svangerskapspenger().isEmpty()
            || !sakerFraFpoversikt.engangsstønad().isEmpty();
    }

    @Override
    public Optional<AnnenPartVedtak> annenPartVedtak(AnnenPartVedtakIdentifikator annenPartVedtakIdentifikator) {
        return connectionFpinfo.annenPartVedtak(annenPartVedtakIdentifikator);
    }

    @Override
    public String ping() {
        return connectionFpinfo.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsynConnection=" + connectionFpinfo + "]";
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
}
