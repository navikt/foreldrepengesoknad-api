package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.regler.uttak.konfig.StandardKonfigurasjon.SØKNADSDIALOG;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.regler.uttak.beregnkontoer.StønadskontoRegelOrkestrering;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.BeregnKontoerGrunnlag;
import no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad;
import no.nav.foreldrepenger.regler.uttak.felles.grunnlag.Stønadskontotype;
import no.nav.foreldrepenger.selvbetjening.http.UnprotectedRestController;

@UnprotectedRestController(UttakController.UTTAK)
public class UttakController {

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
            @RequestParam(name = "fødselsdato", required = false) LocalDate fødselsdato,
            @RequestParam(name = "termindato", required = false) LocalDate termindato,
            @RequestParam(name = "omsorgsovertakelseDato", required = false) LocalDate omsorgsovertakelseDato,
            @RequestParam(name = "startdatoUttak", required = false) LocalDate startdatoUttak,
            @RequestParam(name = "dekningsgrad", required = true) Dekningsgrad dekningsgrad) {

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
