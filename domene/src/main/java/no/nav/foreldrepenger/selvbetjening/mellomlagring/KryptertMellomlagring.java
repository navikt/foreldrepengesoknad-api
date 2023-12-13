package no.nav.foreldrepenger.selvbetjening.mellomlagring;

import static no.nav.foreldrepenger.selvbetjening.vedlegg.DelegerendeVedleggSjekker.DELEGERENDE;
import static no.nav.foreldrepenger.selvbetjening.vedlegg.Image2PDFConverter.megabytes;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import no.nav.foreldrepenger.selvbetjening.vedlegg.VedleggSjekker;

@Service
public class KryptertMellomlagring {
    private static final Logger LOG = LoggerFactory.getLogger(KryptertMellomlagring.class);
    private static final String SØKNAD = "soknad";
    private static final Gson GSON = new Gson();
    private final Mellomlagring mellomlagring;
    private final MellomlagringKrypto krypto;
    private final VedleggSjekker sjekker;

    public KryptertMellomlagring(Mellomlagring mellomlagring,
                                 MellomlagringKrypto krypto,
                                 @Qualifier(DELEGERENDE) VedleggSjekker sjekker) {
        this.mellomlagring = mellomlagring;
        this.krypto = krypto;
        this.sjekker = sjekker;
    }

    public Optional<String> lesKryptertSøknad(Ytelse ytelse) {
        var gammel = mellomlagring.les(krypto.mappenavn(), utledNøkkel(ytelse), false); // Deprecated
        if (gammel.isPresent()) {
            LOG.info("Gammel mellomlagret søknad erstattes med ny mappestruktur for ytelse {}", ytelse);
            mellomlagring.lagre(ytelsespesifikkMappe(ytelse), SØKNAD, gammel.get(), true);
            mellomlagring.slett(krypto.mappenavn(), utledNøkkel(ytelse), false);
            return gammel.map(krypto::decrypt);
        }
        return mellomlagring.les(ytelsespesifikkMappe(ytelse), SØKNAD, true)
            .map(krypto::decrypt);
    }

    public void lagreKryptertSøknad(String søknad, Ytelse ytelse) {
        mellomlagring.lagre(ytelsespesifikkMappe(ytelse), SØKNAD, krypto.encrypt(søknad), true);
    }

    public void slettKryptertSøknad(Ytelse ytelse) {
        mellomlagring.slett(krypto.mappenavn(), utledNøkkel(ytelse), false); // Deprecated
        mellomlagring.slett(ytelsespesifikkMappe(ytelse), SØKNAD, true);
    }


    /*
        I en overgangsfase slår vi opp vedlegg på følgende måte:
        1) Slår opp på gammelt format og ser om det ligger noe vedlegg der. Erstatter disse med ytelse spesifikk navn.
           Neste gang en slår opp dette vedlegget vil vi finne det enten i punkt 2 eller 3.
        2) Slår opp nytt format, men IKKE_OPPGITT. Mapper disse til riktig ytelse hvis oppgitt.
           Neste gang en slår opp dette vedlegget vil vi finne det i punkt 3.
        3) Slår opp på spesifisert ytelse. Når vi dette punktet er vi ferdig migrert og vil alltid finne mellomlagringen i dette punktket (frem til den er slettet).
     */
    public Optional<Attachment> lesKryptertVedlegg(String key, Ytelse ytelse) {
        var gammel = mellomlagring.les(krypto.mappenavn(), key, false); // Deprecated
        if (gammel.isPresent()) {
            LOG.info("Gammelt mellomlagret vedlegg erstattes med ny mappestruktur for ytelse {}", ytelse);
            mellomlagring.lagre(ytelsespesifikkMappe(ytelse), key, gammel.get(), true);
            mellomlagring.slett(krypto.mappenavn(), key, false);
            return gammel
                .map(krypto::decrypt)
                .map(v -> GSON.fromJson(v, Attachment.class));
        }

        var ikkeOppgitt  = mellomlagring.les(ytelsespesifikkMappe(Ytelse.IKKE_OPPGITT), key, true); // Deprecated
        if (ikkeOppgitt.isPresent()) {
            if (!ytelse.equals(Ytelse.IKKE_OPPGITT)) {
                LOG.info("Ersatter mellomlagret vedlegg under IKKE_OPPGITT med oppgitt ytelsen {}", ytelse);
                mellomlagring.lagre(ytelsespesifikkMappe(ytelse), key, ikkeOppgitt.get(), true);
                mellomlagring.slett(ytelsespesifikkMappe(Ytelse.IKKE_OPPGITT), key, true);
            }
            return ikkeOppgitt
                .map(krypto::decrypt)
                .map(v -> GSON.fromJson(v, Attachment.class));
        }

        return mellomlagring.les(ytelsespesifikkMappe(ytelse), key, true)
            .map(krypto::decrypt)
            .map(v -> GSON.fromJson(v, Attachment.class));
    }

    public void lagreKryptertVedlegg(Attachment vedlegg, Ytelse ytelse) {
        LOG.info("Mellomlagrer vedlegg med opprinnelig PDF størrelse lik {}MB", megabytes(vedlegg.bytes));
        sjekker.sjekk(vedlegg);
        mellomlagring.lagre(ytelsespesifikkMappe(ytelse), vedlegg.getUuid(), krypto.encrypt(GSON.toJson(vedlegg)), true);
    }

    public void slettKryptertVedlegg(String uuid, Ytelse ytelse) {
        if (uuid != null) {
            mellomlagring.slett(krypto.mappenavn(), uuid, false); // Deprecated
            mellomlagring.slett(ytelsespesifikkMappe(Ytelse.IKKE_OPPGITT), uuid, true); // Deprecated
            mellomlagring.slett(ytelsespesifikkMappe(ytelse), uuid, true);
        }
    }

    public void slettMellomlagring(Ytelse ytelse) {
        mellomlagring.slettAll(ytelsespesifikkMappe(ytelse));
    }

    private String ytelsespesifikkMappe(Ytelse ytelse) {
        return krypto.mappenavn() + "/" + ytelse.name() + "/";
    }

    private static String utledNøkkel(Ytelse ytelse) {
        return switch (ytelse) {
            case FORELDREPENGER, SVANGERSKAPSPENGER, ENGANGSSTONAD -> ytelse.name();
            case IKKE_OPPGITT -> SØKNAD;
        };
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[storage=" + mellomlagring + ", crypto=" + krypto + ", sjekker=" + sjekker + "]";
    }
}
