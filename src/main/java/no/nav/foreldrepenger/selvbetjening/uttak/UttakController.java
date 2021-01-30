package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.regler.uttak.konfig.StandardKonfigurasjon.SØKNADSDIALOG;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.lang.NonNull;
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
    public Map<Stønadskontotype, Integer> personinfo(@NonNull @RequestParam("antallBarn") int antallBarn,
            @NonNull @RequestParam("morHarRett") boolean morHarRett,
            @NonNull @RequestParam("farHarRett") boolean farHarRett,
            @RequestParam(name = "morHarAleneomsorg", defaultValue = "false") boolean morHarAleneomsorg,
            @RequestParam(name = "farHarAleneomsorg", defaultValue = "false") boolean farHarAleneomsorg,
            @NonNull @RequestParam("fødselsdato") LocalDate fødselsdato,
            @RequestParam("termindato") LocalDate termindato,
            @RequestParam("omsorgsovertakelseDato") LocalDate omsorgsovertakelseDato,
            @RequestParam("startdatoUttak") LocalDate startdatoUttak,
            @NonNull @RequestParam("dekningsgrad") Dekningsgrad dekningsgrad) {

        var b = new BeregnKontoerGrunnlag.Builder()
                .medAntallBarn(antallBarn)
                .medDekningsgrad(dekningsgrad)
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
