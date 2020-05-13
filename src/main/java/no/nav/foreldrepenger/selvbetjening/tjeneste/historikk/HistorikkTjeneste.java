package no.nav.foreldrepenger.selvbetjening.tjeneste.historikk;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.isDevOrLocal;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.tjeneste.oppslag.domain.Fødselsnummer;

@Service
@ConditionalOnProperty(name = "stub.historikk", havingValue = "false", matchIfMissing = true)
public class HistorikkTjeneste implements Historikk, EnvironmentAware {
    private final HistorikkConnection connection;
    private Environment env;

    public HistorikkTjeneste(HistorikkConnection connection) {
        this.connection = connection;
    }

    @Override
    public List<HistorikkInnslag> hentHistorikk() {
        return connection.hentHistorikk();
    }

    @Override
    public List<String> manglendeVedlegg(String saksnr) {
        return connection.manglendeVedlegg(saksnr);
    }

    @Override
    public List<HistorikkInnslag> hentHistorikkFor(Fødselsnummer fnr) {
        if (isDevOrLocal(env)) {
            return connection.hentHistorikk(fnr);
        }
        throw new IllegalStateException("Eksplisitt bruk av FNR ikke støttet i produksjon");
    }

    @Override
    public String ping() {
        return connection.ping();
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [connection=" + connection + "]";
    }

}
