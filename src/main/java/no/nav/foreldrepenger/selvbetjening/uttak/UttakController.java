package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.regler.uttak.konfig.StandardKonfigurasjon.SØKNADSDIALOG;import static java.text.DateFormat.
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.regler.uttak.beregnkontoer.StønadskontoRegelOrkestrering;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.BeregnKontoerGrunnlag;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad;
import no.nav.foreldrepenger.regler.uttak.felles.grunnlag.Stønadskontotype;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
@RequestMapping(UttakController.UTTAK)
@Unprotected
public class UttakController {

    private static final Logger LOG = LoggerFactory.getLogger(UttakController.class);

    private final StønadskontoRegelOrkestrering kontoCalculator;

    static final String UTTAK = "/rest/uttak";

    @Inject
    public UttakController() {
        this.kontoCalculator = new StønadskontoRegelOrkestrering();
    }

    @GetMapping
    public Map<Stønadskontotype, Integer> kontoer(
            @RequestParam(name = "antallBarn", required = true, defaultValue = "1") int antallBarn,
            @RequestParam(name = "morHarRett", required = true) boolean morHarRett,
            @RequestParam(name = "farHarRett", required = true) boolean farHarRett,
            @RequestParam(name = "morHarAleneomsorg", required = false, defaultValue = "false") boolean morHarAleneomsorg,
            @RequestParam(name = "farHarAleneomsorg", required = false, defaultValue = "false") boolean farHarAleneomsorg,
            @DateTimeFormat(pattern = "YYYYMMdd") @RequestParam(name = "fødselsdato", required = false) LocalDate fødselsdato,
            @RequestParam(name = "termindato", required = false) LocalDate termindato,
            @RequestParam(name = "omsorgsovertakelseDato", required = false) LocalDate omsorgsovertakelseDato,
            @RequestParam(name = "startdatoUttak", required = false) LocalDate startdatoUttak,
            @RequestParam(name = "dekningsgrad", required = true) Dekningsgrad dekningsgrad) {

        LOG.info("Beregner konti");
        var b = new BeregnKontoerGrunnlag.Builder()
                .medAntallBarn(antallBarn)
                .medDekningsgrad(dekningsgrad)
                .morAleneomsorg(morHarAleneomsorg)
                .farAleneomsorg(farHarAleneomsorg)
                .farRett(farHarRett)
                .morRett(morHarRett);
        Optional.ofNullable(fødselsdato)
                .ifPresent(d -> b.medFødselsdato(d));
        Optional.ofNullable(termindato)
                .ifPresent(d -> b.medTermindato(d));
        Optional.ofNullable(omsorgsovertakelseDato)
                .ifPresent(d -> b.medOmsorgsovertakelseDato(d));
        return kontoCalculator.beregnKontoer(b.build(), SØKNADSDIALOG).getStønadskontoer();

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [kontoCalculator=" + kontoCalculator + "]";
    }
}
