package no.nav.foreldrepenger.selvbetjening.uttak;

import static no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad.DEKNINGSGRAD_100;
import static no.nav.foreldrepenger.regler.uttak.beregnkontoer.grunnlag.Dekningsgrad.DEKNINGSGRAD_80;
import static no.nav.foreldrepenger.regler.uttak.konfig.StandardKonfigurasjon.SØKNADSDIALOG;

import java.time.LocalDate;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    private static final String FMT = "yyyyMMdd";

    private final StønadskontoRegelOrkestrering kalkulator;

    static final String UTTAK = "/konto";

    @Inject
    public UttakController() {
        this.kalkulator = new StønadskontoRegelOrkestrering();
    }

    @GetMapping
    @CrossOrigin(origins = "*", methods ="GET,POST", exposedHeaders="Location")
    public Map<String, Map<Stønadskontotype, Integer>> kontoer(
            @RequestParam("antallBarn") int antallBarn,
            @RequestParam("morHarRett") boolean morHarRett,
            @RequestParam("farHarRett") boolean farHarRett,
            @RequestParam(name = "morHarAleneomsorg", required = false, defaultValue = "false") boolean morHarAleneomsorg,
            @RequestParam(name = "farHarAleneomsorg", required = false, defaultValue = "false") boolean farHarAleneomsorg,
            @DateTimeFormat(pattern = FMT) @RequestParam(name = "fødselsdato", required = false) LocalDate fødselsdato,
            @DateTimeFormat(pattern = FMT) @RequestParam(name = "termindato", required = false) LocalDate termindato,
            @DateTimeFormat(pattern = FMT) @RequestParam(name = "omsorgsovertakelseDato", required = false) LocalDate omsorgsovertakelseDato,
            @DateTimeFormat(pattern = FMT) @RequestParam("dekningsgrad") String dekningsgrad) {

        return Map.of("kontoer", kalkulator.beregnKontoer(new BeregnKontoerGrunnlag.Builder()
                .medAntallBarn(antallBarn)
                .medDekningsgrad(dekningsgrad(dekningsgrad))
                .morAleneomsorg(morHarAleneomsorg)
                .farAleneomsorg(farHarAleneomsorg)
                .morRett(morHarRett)
                .farRett(farHarRett)
                .medFødselsdato(fødselsdato)
                .medOmsorgsovertakelseDato(omsorgsovertakelseDato)
                .medTermindato(termindato)
                .build(), SØKNADSDIALOG).getStønadskontoer());
    }

    private static Dekningsgrad dekningsgrad(String dekningsgrad) {
        return switch (dekningsgrad) {
            case "100" -> DEKNINGSGRAD_100;
            case "80" -> DEKNINGSGRAD_80;
            default -> throw new IllegalArgumentException("Ugyldig dekningsgrad " + dekningsgrad);
        };
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [kalkulator=" + kalkulator + "]";
    }
}
