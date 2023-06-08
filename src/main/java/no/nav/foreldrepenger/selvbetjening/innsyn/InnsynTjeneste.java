package no.nav.foreldrepenger.selvbetjening.innsyn;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.common.innsyn.AnnenPartVedtak;
import no.nav.foreldrepenger.common.innsyn.Saker;

@Service
public class InnsynTjeneste implements Innsyn, EnvironmentAware {

    private final InnsynConnection connectionFpinfo;
    private final OversiktConnection connectionFpoversikt;
    private final ObjectMapper mapper;

    private static final Logger LOG = LoggerFactory.getLogger(InnsynTjeneste.class);
    private static final Logger SECURE_LOGGER = LoggerFactory.getLogger("secureLogger");
    private Environment env;

    public InnsynTjeneste(InnsynConnection connectionFpinfo,
                          OversiktConnection connectionFpoversikt,
                          ObjectMapper mapper) {
        this.connectionFpinfo = connectionFpinfo;
        this.connectionFpoversikt = connectionFpoversikt;
        this.mapper = mapper;
    }

    @Override
    public Saker hentSaker() {
        LOG.info("Henter saker for pålogget bruker");
        var sakerFraFpinfo = connectionFpinfo.hentSaker();
//        var sakerFraFpoversikt = hentSakerFraFpoversikt();
//        sammenlignSakerFraOversiktOgFpinfoFailSafe(sakerFraFpinfo, sakerFraFpoversikt);
//        return EnvUtil.isProd(env) ? sakerFraFpinfo : sakerFraFpoversikt;
        return sakerFraFpinfo;
    }

    private Saker hentSakerFraFpoversikt() {
        try {
            LOG.info("Henter saker for pålogget bruker fra fpoversikt");
            return connectionFpoversikt.hentSaker();
        } catch (Exception e) {
            LOG.info("Noe gikk galt med henting av saker fra fpoversikt", e);
            return null;
        }
    }

    private void sammenlignSakerFraOversiktOgFpinfoFailSafe(Saker sakerFraFpinfo, Saker sakerFraFpoversikt) {
        try {
            if (sakerFraFpinfo.equals(sakerFraFpoversikt)) {
                LOG.info("Ingen avvik funnet ved sammenligning av saker hentet fra fpinfo og fpoversikt");
            } else {
                LOG.info("Avvik funnet ved sammenligning av saker hentet fra fpinfo og fpoversikt. Ulikt antall {}",
                    uliktAntallSaker(sakerFraFpinfo, sakerFraFpoversikt));
                SECURE_LOGGER.info("""
                    Avvik funnet ved sammenligning av saker hentet fra fpinfo og fpoversikt.
                    Fpinfo {}
                    Fpoversikt {}
                    """, serialize(sakerFraFpinfo), serialize(sakerFraFpoversikt));
            }
        } catch (Exception e) {
            LOG.info("Noe gikk galt med henting eller sammenligning av saker fra fpoversikt", e);
        }
    }

    private boolean uliktAntallSaker(Saker sakerFraFpinfo, Saker sakerFraFpoversikt) {
        return sakerFraFpinfo.engangsstønad().size() != sakerFraFpoversikt.engangsstønad().size()
            || sakerFraFpinfo.foreldrepenger().size() != sakerFraFpoversikt.foreldrepenger().size()
            || sakerFraFpinfo.svangerskapspenger().size() != sakerFraFpoversikt.svangerskapspenger().size();
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
                    """, serialize(annenPartVedtakFpinfo.get()), serialize(annenpartsVedtakFpoversikt.get()));
            }
        } catch (Exception e) {
            LOG.info("Noe gikk galt med henting eller sammenligning av annenparts vedtak fra fpoversikt", e);
        }
    }

    public String serialize(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }

    @Override
    public String ping() {
        return connectionFpinfo.ping();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsynConnection=" + connectionFpinfo + "]";
    }
}
