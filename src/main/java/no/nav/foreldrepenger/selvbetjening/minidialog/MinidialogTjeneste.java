package no.nav.foreldrepenger.selvbetjening.minidialog;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.isDevOrLocal;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.selvbetjening.historikk.MinidialogInnslag;
import no.nav.foreldrepenger.selvbetjening.oppslag.domain.Fødselsnummer;

@Service
@ConditionalOnProperty(name = "stub.minidialog", havingValue = "false", matchIfMissing = true)
public class MinidialogTjeneste implements Minidialog, EnvironmentAware {
    private final MinidialogConnection connection;
    private Environment env;

    public MinidialogTjeneste(MinidialogConnection connection) {
        this.connection = connection;
    }

    @Override
    public List<MinidialogInnslag> hentMinidialoger(Fødselsnummer fnr, boolean activeOnly) {
        if (isDevOrLocal(env)) {
            return connection.hentMinidialoger(fnr, activeOnly);
        }
        throw new IllegalStateException("Eksplisitt bruk av FNR ikke støttet i produksjon");
    }

    @Override
    public List<MinidialogInnslag> aktive() {
        return connection.hentAktiveSpørsmål();
    }

    @Override
    public List<MinidialogInnslag> hentAktiveMinidialogSpørsmål(Fødselsnummer fnr) {
        if (isDevOrLocal(env)) {
            return connection.hentAktiveSpørsmål(fnr);
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
